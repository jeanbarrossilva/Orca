package com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.test

import com.jeanbarrossilva.orca.core.feed.profile.toot.style.styling.mention.MentionDelimiter

internal object ColonMentionDelimiter : MentionDelimiter() {
    override val regex = Regex(":[a-zA-Z0-9._%+-]+")

    override fun onGetTarget(match: String): String {
        return match.removePrefix(":")
    }

    override fun onTarget(target: String): String {
        return ":$target"
    }
}
