package com.ubb.alexandrustoica.reporter

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.ubb.alexandrustoica.reporter.activity.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityUITest {

    @get:Rule
    public val welcomeActivity: ActivityTestRule<LoginActivity> =
            ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    private fun typeData(username: String, password: String) {
        onView(withId(R.id.usernameEditText))
                .perform(typeText(username))
                .perform(closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
                .perform(typeText(password))
                .perform(closeSoftKeyboard())
    }

    @Test
    fun `whenLoggingIn_WithCorrectData_ExpectReportsActivity`() {
        // given:
        typeData("usertest", "usertest")
        // when:
        onView(withId(R.id.loginButton))
                .perform(click())
        // then:
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun `whenLoggingIn_WithIncorrectData_ExpectSimpleAlertDialog`() {
        // given:
        typeData("not_a_valid_username",
                "not_a_valid_password")
        // when:
        onView(withId(R.id.loginButton))
                .perform(click())
        // then:
        onView(withId(R.id.alertTitle)).check(matches(isDisplayed()))
    }
}
