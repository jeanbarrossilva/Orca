/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.thumbnail

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey

/** [SemanticsPropertyKey] returned by [Shape]. */
private val shapeSemanticsPropertyKey = SemanticsPropertyKey<Shape>("Shape")

/** [Shape] by which a [Thumbnail] is clipped. */
internal val SemanticsProperties.Shape
  get() = shapeSemanticsPropertyKey
