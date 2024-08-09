/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.avatar.sample
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.core.sample.feed.profile.post.content.samples
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import kotlin.test.Test

internal class DispositionTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenGettingDispositionWithoutAttachments() {
    val postProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
    Disposition.of(
      GalleryPreview.createSample(postProvider)
        .copy(postProvider.provideOneCurrent().id, Author.sample.name, attachments = emptyList()),
      Disposition.OnThumbnailClickListener.empty
    )
  }

  @Test
  fun getsSingleDispositionForOneAttachment() {
    val postProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
    assertThat(
        Disposition.of(
          GalleryPreview.createSample(postProvider).copy(attachments = Attachment.samples.take(1))
        )
      )
      .isEqualTo(
        Disposition.Single(
          GalleryPreview.createSample(postProvider).copy(attachments = Attachment.samples.take(1))
        )
      )
  }

  @Test
  fun getsGridDispositionForMultipleThumbnails() {
    val postProvider =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
        .postProvider
    assertThat(
        Disposition.of(
          GalleryPreview.createSample(postProvider).copy(attachments = Attachment.samples)
        )
      )
      .isEqualTo(Disposition.Grid(GalleryPreview.createSample(postProvider)))
  }
}
