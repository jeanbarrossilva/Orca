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

package com.jeanbarrossilva.orca.core.feed.profile.post

import kotlinx.coroutines.flow.Flow

/** Provides [Post]s. */
abstract class PostProvider {
  /**
   * Provides the [Post] identified as [id].
   *
   * @param id ID of the [Post] to be provided.
   * @see Post.id
   */
  abstract suspend fun provide(id: String): Flow<Post>
}
