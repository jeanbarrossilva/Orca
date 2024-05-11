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

package br.com.orcinus.orca.core.mastodon.network.requester.request.memory

import kotlinx.coroutines.test.runTest
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] from which an [InMemoryRequestDao] can be obtained, which will also be cleared
 * at the end of every test case.
 *
 * @see dao
 */
internal class InMemoryRequestDaoTestRule : ExternalResource() {
  /** [InMemoryRequestDao] to be cleared after the tests finish running. */
  val dao = InMemoryRequestDao()

  override fun after() {
    super.after()
    runTest { dao.clear() }
  }
}
