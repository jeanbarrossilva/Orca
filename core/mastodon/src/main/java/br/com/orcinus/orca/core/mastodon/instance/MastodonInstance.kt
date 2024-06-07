/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.instance

import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.instance.requester.Logger
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequest
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.set

/** A [MastodonInstance] with a generic [Authorizer] and an [Authenticator]. */
typealias SomeMastodonInstance = MastodonInstance<*, *>

/**
 * [Instance] that performs all of its underlying operations by sending [HttpRequest]s to the API.
 *
 * @param F [Authorizer] with which authorization will be performed.
 * @param S [Authenticator] to authenticate the user with.
 * @param authorizer [Authorizer] by which the user will be authorized.
 */
abstract class MastodonInstance<F : Authorizer, S : Authenticator>
internal constructor(final override val domain: Domain, internal val authorizer: F) :
  Instance<S>() {
  /** [Url] to which routes will be appended when [HttpRequest]s are sent. */
  internal val url = URLBuilder().apply { set(scheme = "https", host = "$domain") }.build()

  /** [HttpClient] by which [HttpRequest]s will be sent. */
  internal open val requester =
    Requester(Logger.Android, baseURI = domain.uri, clientEngineFactory = CIO)
}
