package com.jeanbarrossilva.orca.std.styledstring.type

import com.jeanbarrossilva.orca.std.styledstring.Style

/** [Style] for a specific subject. */
data class Hashtag(override val indices: IntRange) : Style.Constrained() {
  override val regex = Regex("\\w+")

  override fun at(indices: IntRange): Hashtag {
    return copy(indices = indices)
  }
}
