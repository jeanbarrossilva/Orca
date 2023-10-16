package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverter
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/**
 * [ProfileConverter] that converts a [Profile], regardless of its type, into a
 * [ProfileDetails.Default].
 */
internal class DefaultProfileConverter(override val next: ProfileConverter?) : ProfileConverter() {
  override fun onConvert(profile: Profile, colors: Colors): ProfileDetails {
    return ProfileDetails.Default(
      profile.id,
      profile.avatarLoader,
      profile.name,
      profile.account,
      profile.bio.toAnnotatedString(colors),
      profile.url
    )
  }
}
