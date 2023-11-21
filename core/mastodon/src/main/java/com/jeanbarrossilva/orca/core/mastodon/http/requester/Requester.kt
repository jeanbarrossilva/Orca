package com.jeanbarrossilva.orca.core.mastodon.http.requester

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.mastodon.http.client.CoreHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.Parameters
import io.ktor.http.content.PartData
import io.ktor.http.headersOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.job

/**
 * Manages the sending, repetition, cancellation and continuity of asynchronous HTTP requests.
 *
 * @see Request
 * @see Request.Get
 * @see Request.Post
 */
abstract class Requester internal constructor() {
  /** [CoroutineScope] in which the requests are performed. */
  protected open val coroutineScope = CoroutineScope(Dispatchers.IO)

  /**
   * Requests that are currently being performed, associated to the route to which they were sent.
   */
  protected val ongoing = hashMapOf<String, Job>()

  /** Requests that have been interrupted unexpectedly and are retained for later execution. */
  protected open val retained = hashSetOf<Request>()

  /** [CoreHttpClient] to which requests are sent. */
  abstract val client: HttpClient

  /**
   * [CancellationException] that denotes that an operation was intentionally interrupted by the
   * user. Indicates, overall, that the request that threw this shouldn't be retained for later
   * execution.
   */
  private class UnretainableCancellationException : CancellationException()

  /**
   * Sends a GET request to the [route].
   *
   * @param route [String] that's the path for the resource to be obtained.
   * @param parameters [Parameters] to be appended to the final URL.
   * @param headers [Headers] with which the request will be sent.
   */
  suspend fun get(
    route: String,
    parameters: Parameters = Parameters.Empty,
    headers: Headers = headersOf()
  ): HttpResponse {
    return coroutineScope
      .async { onGet(route, parameters, headers) }
      .retainOnCancellation { Request.Get(route, parameters, headers) }
      .ongoing(route, Deferred<HttpResponse>::await)
  }

  /**
   * Sends a POST request to the [route].
   *
   * @param route [String] that's the path to which the request will be sent.
   * @param parameters [Parameters] to be appended to the final URL.
   * @param headers [Headers] with which the request will be sent.
   * @param form Multipart form data.
   */
  suspend fun post(
    route: String,
    parameters: Parameters = Parameters.Empty,
    headers: Headers = headersOf(),
    form: List<PartData> = emptyList()
  ): HttpResponse {
    return coroutineScope
      .async { onPost(route, parameters, headers, form) }
      .retainOnCancellation { Request.Post(route, parameters, headers, form) }
      .ongoing(route, Deferred<HttpResponse>::await)
  }

  /**
   * Tries to re-send all requests that are currently retained.
   *
   * @see retained
   */
  suspend fun resume() {
    retained.forEach { it.sendTo(this) }
  }

  /** Cancels all ongoing requests. */
  fun cancelAll() {
    ongoing.keys.forEach(::cancel)
  }

  /**
   * Cancels the ongoing request sent to the given [route].
   *
   * @param route Path to which the request was sent.
   */
  fun cancel(route: String) {
    if (route in ongoing) {
      coroutineScope.coroutineContext.job.cancel(UnretainableCancellationException())
      ongoing.remove(route)
    }
  }

  /**
   * Callback run whenever a GET request is sent to the [route].
   *
   * @param route [String] that's the path for the resource to be obtained.
   * @param parameters [Parameters] to be appended to the final URL.
   * @param headers [Headers] with which the request is being sent.
   */
  protected abstract suspend fun onGet(
    route: String,
    parameters: Parameters,
    headers: Headers
  ): HttpResponse

  /**
   * Callback run whenever a POST request is sent to the [route].
   *
   * @param route [String] that's the path to which the request is being sent.
   * @param headers [Headers] with which the request is being sent.
   * @param parameters [Parameters] to be appended to the final URL.
   * @param form Multipart form data.
   */
  protected abstract suspend fun onPost(
    route: String,
    parameters: Parameters,
    headers: Headers,
    form: List<PartData>
  ): HttpResponse

  /**
   * Retains the result of [request] if this [Job] gets cancelled.
   *
   * @param T Job whose completion will be listened to.
   * @param request Lazily creates the [Request] to be retained.
   */
  private fun <T : Job> T.retainOnCancellation(request: () -> Request): T {
    invokeOnCompletion {
      val isRetainable = it is CancellationException && it !is UnretainableCancellationException
      if (isRetainable) {
        retained.add(request())
      }
    }
    return this
  }

  /**
   * Marks this [Job] as that of an ongoing request before the [action] is run and unmarks it as
   * such after it's finished executing.
   *
   * @param route Path to which the request is being sent.
   * @param action Operation to be run when this [Job] is considered to be ongoing.
   */
  private suspend fun <I : Job, O> I.ongoing(route: String, action: suspend I.() -> O): O {
    ongoing[route] = this
    return action().also { ongoing.remove(route) }
  }

  companion object {
    /**
     * [UnauthenticatedRequester]s associated to the [HttpClient] with which they have been
     * initially created.
     */
    private val creations = hashMapOf<HttpClient, UnauthenticatedRequester>()

    /**
     * Creates or retrieves a [Requester] that sends HTTP requests as an
     * [unauthenticated][Actor.Unauthenticated] [Actor] by default through the given [client].
     *
     * @param client [CoreHttpClient] through which requests will be sent.
     * @see UnauthenticatedRequester
     * @see UnauthenticatedRequester.authenticated
     */
    fun through(client: HttpClient): UnauthenticatedRequester {
      return creations.getOrPut(client) { UnauthenticatedRequester(client) }
    }

    /** Removes all created [Requester]s. */
    fun clear() {
      creations.values.forEach(UnauthenticatedRequester::clear)
      creations.clear()
    }
  }
}
