package com.jeanbarrossilva.orca.feature.profiledetails.test

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity

internal class ProfileDetailsActivity : SingleFragmentActivity() {
  override val route = "profile-details"

  override fun NavGraphBuilder.add() {
    fragment<ProfileDetailsFragment>()
  }

  companion object {
    fun getIntent(backwardsNavigationState: BackwardsNavigationState, id: String): Intent {
      val context = InstrumentationRegistry.getInstrumentation().context
      return Intent<ProfileDetailsActivity>(
        context,
        ProfileDetailsFragment.BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState,
        ProfileDetailsFragment.ID_KEY to id
      )
    }
  }
}
