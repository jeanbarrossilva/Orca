package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.jeanbarrossilva.orca.app.R
import com.jeanbarrossilva.orca.app.demo.test.PlatformDialogDismissalTestRule
import com.jeanbarrossilva.orca.app.demo.test.browsesTo
import com.jeanbarrossilva.orca.app.demo.test.ok
import com.jeanbarrossilva.orca.app.demo.test.performStartClick
import com.jeanbarrossilva.orca.app.demo.test.waitUntil
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.headline.onHeadlineCards
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

internal class ProfileDetailsTests {
  private val intentsRule = IntentsRule()
  private val composeRule = createAndroidComposeRule<DemoOrcaActivity>()
  private val platformDialogDismissalRule = PlatformDialogDismissalTestRule()

  @get:Rule
  val ruleChain: RuleChain? =
    RuleChain.outerRule(intentsRule).around(composeRule).around(platformDialogDismissalRule)

  @Test
  fun navigatesToTootHighlight() {
    var hasNavigated = false
    intending(browsesTo("${Highlight.sample.url}")).ok { hasNavigated = true }
    composeRule.onHeadlineCards().onFirst().performClick()
    waitUntil { hasNavigated }
  }

  @Test
  fun navigatesToTootDetailsOnTootPreviewClick() {
    onView(withId(R.id.profile_details)).perform(click())
    composeRule.onTootPreviews().onFirst().performStartClick()
    assertIsAtFragment(composeRule.activity, TootDetailsFragment.getRoute(Toot.sample.id))
  }
}
