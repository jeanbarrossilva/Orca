package com.jeanbarrossilva.mastodon.feature.profile.ui

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
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.mastodonte.core.profile.AnyProfile
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString
import com.jeanbarrossilva.mastodonte.platform.ui.profile.LargeAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.profile.sample

@Composable
internal fun Header(modifier: Modifier = Modifier) {
    Header(
        avatar = { LargeAvatar() },
        name = { MediumTextualPlaceholder() },
        account = { LargeTextualPlaceholder() },
        bio = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(3) { LargeTextualPlaceholder() }
                MediumTextualPlaceholder()
            }
        },
        modifier
    )
}

@Composable
internal fun Header(profile: AnyProfile, modifier: Modifier = Modifier) {
    Header(
        avatar = { LargeAvatar(profile.name, profile.avatarURL) },
        name = { Text(profile.name) },
        account = { Text("${profile.account.username}@${profile.account.instance}") },
        bio = { Text(HtmlAnnotatedString(profile.bio)) },
        modifier
    )
}

@Composable
private fun Header(
    avatar: @Composable () -> Unit,
    name: @Composable () -> Unit,
    account: @Composable () -> Unit,
    bio: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(MastodonteTheme.spacings.extraLarge)
            .fillMaxWidth(),
        Arrangement.spacedBy(MastodonteTheme.spacings.extraLarge),
        Alignment.CenterHorizontally
    ) {
        avatar()

        Column(
            verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProvideTextStyle(MastodonteTheme.typography.headlineLarge, name)
            ProvideTextStyle(MastodonteTheme.typography.titleSmall, account)
        }

        ProvideTextStyle(LocalTextStyle.current.copy(textAlign = TextAlign.Center), bio)
    }
}

@Composable
@Preview
private fun LoadingHeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header()
        }
    }
}

@Composable
@Preview
private fun HeaderPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Header(Profile.sample)
        }
    }
}
