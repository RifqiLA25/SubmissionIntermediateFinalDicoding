package com.example.submissionawal.ui.view

import android.content.Intent
import android.widget.ProgressBar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.submissionawal.R
import com.example.submissionawal.help.EspressoIdlingSource
import com.example.submissionawal.help.ProgressBardlingSource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingSource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingSource.countingIdlingResource)
    }

    @Test
    fun testLoginSuccess() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        activityScenario.onActivity {
            val progressBar = it.findViewById<ProgressBar>(R.id.progressIndicator)
            IdlingRegistry.getInstance().register(ProgressBardlingSource(progressBar))
        }


        Espresso.onView(withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("rifqila245@gmail.com"), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.passwordEditTextLayout))
            .perform(ViewActions.typeText("rifqi12345"), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.loginButton)).perform(ViewActions.click())

        Espresso.onView(withText(R.string.succes))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withText(R.string.ok)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.main)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testLogout() {

        activity.scenario.onActivity {
            it.startActivity(Intent(it, MainActivity::class.java))
        }

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)

        Espresso.onView(withText(R.string.logout)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.main0))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}