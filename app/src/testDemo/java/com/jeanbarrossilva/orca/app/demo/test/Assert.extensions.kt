package com.jeanbarrossilva.orca.app.demo.test

import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.junit.Assert.assertNotNull
import org.robolectric.Shadows.shadowOf

/**
 * Asserts that a [Fragment] tagged as [tag] is found within the given [activity].
 *
 * @param activity [FragmentActivity] in which the [Fragment] is supposed to be.
 * @param tag Tag of the [Fragment] to be found.
 **/
internal fun assertIsAtFragment(activity: FragmentActivity, tag: String) {
    shadowOf(Looper.getMainLooper()).idle()
    assertNotNull(
        "Fragment tagged as \"$tag\" not found.",
        activity.supportFragmentManager.findFragmentByTag(tag)
    )
}
