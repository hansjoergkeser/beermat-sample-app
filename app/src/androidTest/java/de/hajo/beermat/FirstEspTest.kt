package de.hajo.beermat

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author hansjoerg.keser
 * @since 26.01.19
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class FirstEspTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun assertButtons() {
        onView(withId(R.id.button_add)).check(matches(isDisplayed()))
        onView(withId(R.id.button_reduce)).check(matches(isDisplayed()))
    }

    @Test
    fun assertTotalPriceOfThreeBeers() {
        onView(withId(R.id.tv_beer)).check(matches(isDisplayed()))
        onView(withId(R.id.et_price)).perform(click()).perform(clearText()).perform(typeText("2.99"))
        onView(withId(R.id.button_add)).perform(doubleClick())
        val expectedTotalPrice = "8,97 €"
        onView(withId(R.id.tv_total_price_of_line)).check(matches(allOf(isDisplayed(), withText(expectedTotalPrice))))
    }

}