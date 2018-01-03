package com.ubb.alexandrustoica.reporter

import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.ubb.alexandrustoica.reporter.activity.RegisterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityUITest {

    @get:Rule
    public val registerActivity: ActivityTestRule<RegisterActivity> =
            ActivityTestRule<RegisterActivity>(RegisterActivity::class.java)


    @Test
    fun `whenSigningUp_WithCorrectData_ExpectUserRegistered`() {
        // given:
        typeData("test", "test", "test@test.com",
                "test", "Test")
        // when:
        onView(withId(R.id.registerButton)).perform(click())
        SystemClock.sleep(10000)
        // then:
        onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()))
    }

    @Test
    fun `whenSigningUp_WithIncorrectPassword_ExpectErrorAlert`() {
        // given:
        typeData("username", "password", "test@test.com",
                "password_", "Name")
        SystemClock.sleep(1000)
        // when:
        onView(withId(R.id.registerButton)).perform(click())
        // then:
        onView(withId(R.id.alertTitle)).check(matches(isDisplayed()))
    }

    private fun typeData(
            username: String, password: String,
            email: String, confirmPassword: String, name: String) {
        onView(withId(R.id.nameEditText))
                .perform(typeText(name))
                .perform(closeSoftKeyboard())
        onView(withId(R.id.usernameEditText))
                .perform(typeText(username))
                .perform(closeSoftKeyboard())
        onView(withId(R.id.emailEditText))
                .perform(typeText(email))
                .perform(closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
                .perform(typeText(password))
                .perform(closeSoftKeyboard())
        onView(withId(R.id.confirmPasswordEditText))
                .perform(typeText(confirmPassword))
                .perform(closeSoftKeyboard())
    }
}
