package com.jeanbarrossilva.orca.core.http.feed

import com.chrynan.paginate.core.loadAllPagesItems
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginateSource
import kotlinx.coroutines.flow.Flow

/** [FeedProvider] that requests the feed's [Toot]s to the API. **/
class HttpFeedProvider internal constructor(private val actorProvider: ActorProvider) :
    FeedProvider() {
    /** [Flow] of paginated [Toot]s to be provided. **/
    private val flow =
        FeedTootPaginateSource.loadAllPagesItems(HttpTootPaginateSource.DEFAULT_COUNT)

    override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
        FeedTootPaginateSource.paginateTo(page)
        return flow
    }

    override suspend fun containsUser(userID: String): Boolean {
        return when (actorProvider.provide()) {
            is Actor.Unauthenticated -> false
            is Actor.Authenticated -> true
        }
    }
}
