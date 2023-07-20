package com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle

import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.test.TestCompleteLifecycleActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LifecycleActivityTests {
    @Test
    fun `GIVEN an activity WHEN creating it THEN its lifecycle state is created`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).create().use {
            assertEquals(CompleteLifecycleState.CREATED, it.get().completeLifecycleState)
        }
    }

    @Test
    fun `GIVEN an activity WHEN starting it THEN its lifecycle state is started`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).start().use {
            assertEquals(CompleteLifecycleState.STARTED, it.get().completeLifecycleState)
        }
    }

    @Test
    fun `GIVEN an activity WHEN resuming it THEN its lifecycle state is resumed`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).resume().use {
            assertEquals(CompleteLifecycleState.RESUMED, it.get().completeLifecycleState)
        }
    }

    @Test
    fun `GIVEN an activity WHEN pausing it THEN its lifecycle state is paused`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).pause().use {
            assertEquals(CompleteLifecycleState.PAUSED, it.get().completeLifecycleState)
        }
    }

    @Test
    fun `GIVEN an activity WHEN stopping it THEN its lifecycle state is stopped`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).stop().use {
            assertEquals(CompleteLifecycleState.STOPPED, it.get().completeLifecycleState)
        }
    }

    @Test
    fun `GIVEN an activity WHEN destroying it THEN its lifecycle state is destroyed`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).destroy().use {
            assertEquals(CompleteLifecycleState.DESTROYED, it.get().completeLifecycleState)
        }
    }

    @Test
    fun `GIVEN an activity WHEN finishing it THEN it's destroyed`() {
        Robolectric.buildActivity(TestCompleteLifecycleActivity::class.java).setup().use {
            it.get().run {
                finish()
                assertEquals(CompleteLifecycleState.DESTROYED, completeLifecycleState)
            }
        }
    }
}
