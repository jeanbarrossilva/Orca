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

package com.jeanbarrossilva.orca.platform.ui.core.image

import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.compose.ComposableImage
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/** [ComposableImageLoader] that loads images from a [SampleImageSource]. */
internal class SampleImageLoader private constructor(override val source: SampleImageSource) :
  ComposableImageLoader<SampleImageSource>() {
  /** [ImageLoader.Provider] that provides a [SampleImageLoader]. */
  object Provider : ComposableImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): SampleImageLoader {
      return SampleImageLoader(source)
    }
  }

  override fun load(): ComposableImage {
    return { contentDescription, shape, contentScale, modifier ->
      Image(
        painterResource(source.resourceID),
        contentDescription,
        modifier.clip(shape),
        contentScale = contentScale
      )
    }
  }
}
