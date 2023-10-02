package com.jeanbarrossilva.orca.core.feed.profile.toot.stat

/** Creates an empty [Stat]. **/
fun <T> emptyStat(): Stat<T> {
    return buildStat(count = 0) {
    }
}

/**
 * Builds a [Stat].
 *
 * @param count Initial amount of elements of the [Stat].
 * @param build Configuration for the [Stat].
 **/
fun <T> buildStat(count: Int, build: Stat.Builder<T>.() -> Unit): Stat<T> {
    return Stat.Builder<T>(count).apply(build).build()
}
