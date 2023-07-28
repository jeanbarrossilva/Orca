package com.jeanbarrossilva.mastodonte.core.mastodon.account

import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import kotlinx.serialization.Serializable

@Serializable
internal data class Relationship(val following: Boolean) {
    fun toFollow(account: MastodonAccount): Follow {
        return when {
            account.locked && following -> Follow.Private.following()
            account.locked -> Follow.Private.unfollowed()
            following -> Follow.Public.following()
            else -> Follow.Public.unfollowed()
        }
    }
}
