/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.instance

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotEmpty
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test

internal class SampleInstanceBuilderTests {
  @Test
  fun buildsSampleInstanceWithoutDefaultProfilesAndPosts() {
    val profiles =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .build()
        .profileProvider
        .provideCurrent()
    assertThat(profiles).isEmpty()
  }

  @Test
  fun buildsSampleInstanceWithDefaultProfilesAndWithoutDefaultPosts() {
    val instance =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider).withDefaultProfiles().build()
    val profiles = instance.profileProvider.provideCurrent()
    val posts = instance.postProvider.provideAllCurrent()
    assertThat(profiles).isNotEmpty()
    assertThat(posts).isEmpty()
  }

  @Test
  fun buildsSampleInstanceWithDefaultProfilesAndPosts() {
    val instance =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
    val profiles = instance.profileProvider.provideCurrent()
    val posts = instance.postProvider.provideAllCurrent()
    assertThat(profiles).isNotEmpty()
    assertThat(posts).isNotEmpty()
  }
}
