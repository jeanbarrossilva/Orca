/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import java.net.URL
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.typeOf

/** [MastodonPostPaginator] that receives a [List] of [MastodonStatus]es from the API. */
internal abstract class MastodonStatusesPaginator : MastodonPostPaginator<List<MastodonStatus>>() {
  /**
   * [ImageLoader.Provider] that provides the [ImageLoader] by which images will be loaded from a
   * [URL].
   */
  protected abstract val imageLoaderProvider: SomeImageLoaderProvider<URL>

  /**
   * [MastodonCommentPaginator.Provider] through which a [MastodonCommentPaginator] for paginating
   * through the comments of the retrieved [Post]s will be provided.
   *
   * @see Post.comment
   */
  protected abstract val commentPaginatorProvider: MastodonCommentPaginator.Provider

  @Suppress("UNCHECKED_CAST")
  final override val kClass = List::class as KClass<List<MastodonStatus>>

  final override fun create(): KType {
    val statusType = typeOf<MastodonStatus>()
    val statusProjection = KTypeProjection(KVariance.OUT, statusType)
    val arguments = listOf(statusProjection)
    return kClass.createType(arguments)
  }

  final override fun List<MastodonStatus>.toPosts(): List<Post> {
    return map { it.toPost(imageLoaderProvider, commentPaginatorProvider) }
  }
}
