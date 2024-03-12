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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.BackAction
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [NavigationBar] for testing purposes. */
const val NAVIGATION_BAR_TAG = "navigation-bar"

/** Default values used by a [NavigationBar]. */
object NavigationBarDefaults {
  /** [Color] by which the container of a [NavigationBar] is colored. */
  val ContainerColor = Color.Black
}

/**
 * Bar to which tabs for accessing different contexts within Orca can be added and/or a prominent
 * navigation action (such as going back to a previous context) may be performed.
 *
 * @param title [AutoSizeText] that describes the current context.
 * @param action Main navigation action.
 * @param modifier [Modifier] to be applied to the underlying [Surface].
 * @param content Tabs that are shown, configured through the provided [NavigationBarScope].
 * @see NavigationBarScope.tab
 */
@Composable
fun NavigationBar(
  title: @Composable () -> Unit,
  action: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  content: NavigationBarScope.() -> Unit
) {
  val scope = remember(content) { NavigationBarScope().apply(content) }
  val spacing = AutosTheme.spacings.medium.dp

  Surface(modifier, color = NavigationBarDefaults.ContainerColor, contentColor = Color.White) {
    Column(Modifier.padding(spacing).testTag(NAVIGATION_BAR_TAG), Arrangement.spacedBy(spacing)) {
      Box(Modifier.fillMaxWidth(), Alignment.Center) {
        ProvideTextStyle(
          AutosTheme.typography.headlineLarge.copy(
            color = LocalContentColor.current,
            textAlign = TextAlign.Center
          )
        ) {
          title()
        }
      }

      Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        action()
        scope.tabs.forEach { it() }
      }
    }
  }
}

/** Preview of a [NavigationBar]. */
@Composable
@MultiThemePreview
private fun NavigationBarPreview() {
  AutosTheme {
    NavigationBar(title = { AutoSizeText("Home") }, action = { BackAction(onClick = {}) }) {
      tab(onClick = {}) {
        Icon(AutosTheme.iconography.home.filled.asImageVector, contentDescription = "Home")
      }

      tab(onClick = {}) {
        Icon(AutosTheme.iconography.profile.outlined.asImageVector, contentDescription = "Profile")
      }

      tab(onClick = {}) {
        Icon(
          AutosTheme.iconography.settings.outlined.asImageVector,
          contentDescription = "Settings"
        )
      }
    }
  }
}
