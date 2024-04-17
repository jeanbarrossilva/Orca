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

package br.com.orcinus.orca.std.injector.module.replacement

import java.util.Objects

/**
 * Implementation of [ReplacementList] that delegates [MutableList]-like functionality to the
 * [delegate] and is returned by [replacementListOf].
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @param delegate [MutableList] to which this [DelegatorReplacementList]'s functionality will be
 *   delegated, except for that of [add].
 */
private class DelegatorReplacementList<E, S>(
  private val delegate: MutableList<E>,
  override val selector: (E) -> S
) : ReplacementList<E, S>(), MutableList<E> by delegate {
  override fun equals(other: Any?): Boolean {
    return other is DelegatorReplacementList<*, *> &&
      delegate == other.delegate &&
      selector == other.selector
  }

  override fun hashCode(): Int {
    return Objects.hash(delegate, selector)
  }

  override fun toString(): String {
    return delegate.toString()
  }

  override fun contains(element: E): Boolean {
    return super.contains(element)
  }

  override fun containsAll(elements: Collection<E>): Boolean {
    return super.containsAll(elements)
  }

  override fun add(element: E): Boolean {
    return super.add(element)
  }

  override fun addAll(elements: Collection<E>): Boolean {
    return super.addAll(elements)
  }

  override fun add(index: Int, element: E) {
    super.add(index, element)
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
    return super.addAll(index, elements)
  }

  override fun onAdd(index: Int, element: E) {
    delegate.add(index, element)
  }
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * When denoting whether one of the elements should be replaced when a new one is being added, the
 * replacement and the current candidate over which iteration is taking place will be compared
 * structurally, meaning that it will be replaced when the replacement is passed into its
 * [Any.equals] method and it returns `true`.
 *
 * @param E Element to be contained.
 * @param elements Elements to be added to the [ReplacementList].
 */
fun <E> replacementListOf(vararg elements: E): ReplacementList<E, E> {
  return replacementListOf(*elements) { it }
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @param elements Elements to be added to the [ReplacementList].
 * @param selector Provides the value by which each element should be compared when replaced.
 */
fun <E, S> replacementListOf(vararg elements: E, selector: (E) -> S): ReplacementList<E, S> {
  val delegate = mutableListOf(*elements)
  return DelegatorReplacementList(delegate, selector)
}
