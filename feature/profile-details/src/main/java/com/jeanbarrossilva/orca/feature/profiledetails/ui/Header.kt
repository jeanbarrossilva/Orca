package com.jeanbarrossilva.orca.feature.profiledetails.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.LargeAvatar
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader

@Composable
internal fun Header(modifier: Modifier = Modifier) {
  Header(
    avatar = { LargeAvatar() },
    name = { MediumTextualPlaceholder() },
    account = { LargeTextualPlaceholder() },
    bio = {
      Column(
        verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        repeat(3) { LargeTextualPlaceholder() }
        MediumTextualPlaceholder()
      }
    },
    mainActionButton = {},
    modifier
  )
}

@Composable
internal fun Header(details: ProfileDetails, modifier: Modifier = Modifier) {
  val avatarLoader = rememberImageLoader(details.avatarURL)

  Header(
    avatar = { LargeAvatar(avatarLoader, details.name) },
    name = { Text(details.name) },
    account = { Text(details.formattedAccount) },
    bio = { Text(details.bio) },
    mainActionButton = { details.MainActionButton() },
    modifier
  )
}

@Composable
private fun Header(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  account: @Composable () -> Unit,
  bio: @Composable () -> Unit,
  mainActionButton: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier.padding(OrcaTheme.spacings.extraLarge).fillMaxWidth(),
    Arrangement.spacedBy(OrcaTheme.spacings.extraLarge),
    Alignment.CenterHorizontally
  ) {
    avatar()

    Column(
      verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ProvideTextStyle(OrcaTheme.typography.headlineLarge, name)
      ProvideTextStyle(OrcaTheme.typography.titleSmall, account)
    }

    ProvideTextStyle(LocalTextStyle.current.copy(textAlign = TextAlign.Center), bio)
    mainActionButton()
  }
}

@Composable
@MultiThemePreview
private fun LoadingHeaderPreview() {
  OrcaTheme { Surface(color = OrcaTheme.colors.background.container) { Header() } }
}

@Composable
@MultiThemePreview
private fun HeaderPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container) { Header(ProfileDetails.sample) }
  }
}
