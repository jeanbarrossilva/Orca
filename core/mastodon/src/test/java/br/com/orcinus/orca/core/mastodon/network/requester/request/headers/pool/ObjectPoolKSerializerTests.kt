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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.pool

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.utils.io.pool.DefaultPool
import io.ktor.utils.io.pool.useInstance
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class ObjectPoolKSerializerTests {
  private val serializer = ObjectPoolKSerializer(Int::class)
  private val objectPool =
    object : DefaultPool<Int>(capacity = 1) {
      override fun produceInstance(): Int {
        return 0
      }
    }

  @AfterTest
  fun tearDown() {
    objectPool.close()
  }

  @Test
  fun serializes() {
    assertThat(Json.encodeToString(serializer, objectPool))
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(serializer.descriptor.getElementName(0), 1)
            put(serializer.descriptor.getElementName(1), 0)
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
            serializer,
            @OptIn(ExperimentalSerializationApi::class)
            buildJsonObject {
                put(serializer.descriptor.getElementName(0), 1)
                put(serializer.descriptor.getElementName(1), 0)
              }
              .toString()
          )
          .useInstance { it }
      )
      .isEqualTo(objectPool.borrow())
  }
}
