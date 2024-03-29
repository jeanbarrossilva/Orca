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

package com.jeanbarrossilva.orca.feature.registration.credentials

import assertk.assertThat
import com.jeanbarrossilva.orca.platform.navigation.navigator
import com.jeanbarrossilva.orca.platform.navigation.test.activity.launchNavigationActivity
import com.jeanbarrossilva.orca.platform.navigation.test.isAt
import kotlin.test.Test

internal class CredentialsFragmentTests {
  @Test
  fun navigates() {
    launchNavigationActivity().use { scenario ->
      scenario.onActivity { activity ->
        CredentialsFragment.navigate(activity.navigator)
        assertThat(activity).isAt(CredentialsFragment.ROUTE)
      }
    }
  }
}
