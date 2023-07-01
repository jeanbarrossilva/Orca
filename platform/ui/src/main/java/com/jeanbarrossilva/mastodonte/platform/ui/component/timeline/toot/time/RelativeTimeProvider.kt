package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time

import java.time.ZonedDateTime

/** Provides a relative version of a [ZonedDateTime]. **/
abstract class RelativeTimeProvider {
    /**
     * Provides the relative version of [dateTime].
     *
     * @param dateTime [ZonedDateTime] whose relative version will be provided.
     **/
    internal abstract fun provide(dateTime: ZonedDateTime): String
}
