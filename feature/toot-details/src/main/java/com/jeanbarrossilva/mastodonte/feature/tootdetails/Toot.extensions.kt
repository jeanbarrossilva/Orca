package com.jeanbarrossilva.mastodonte.feature.tootdetails

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.feature.tootdetails.Toot as _Toot
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.formatted
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.formatted

/** Converts this core [Toot] into a toot-details-domain-specific [Toot][_Toot]. **/
internal fun Toot.toDomainToot(): _Toot {
    return _Toot(
        id,
        author.avatarURL,
        author.name,
        "@${author.account.username}",
        body = HtmlAnnotatedString(content),
        publicationDateTime,
        publicationDateTime.formatted,
        commentCount.formatted,
        favoriteCount.formatted,
        reblogCount.formatted,
        url
    )
}
