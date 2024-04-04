package com.example.filmapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.filmapp.activities.WriteReviewActivity;
import com.example.filmapp.activities.ReviewOverviewActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;



/**
 * Test for different functions and components in the write review activity
 *
 */
@RunWith(AndroidJUnit4.class)
public class WriteReviewTest {

    @Rule
    public IntentsTestRule<WriteReviewActivity> activityRule =
            new IntentsTestRule<>(WriteReviewActivity.class);


    /**
     * checks UI elements displaying properly
     */
    @Test
    public void UIElementsTest() {
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewFirstName)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewLastName)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(matches(isDisplayed()));
    }


    /**
     * checks clickable submit button
     */
    @Test
    public void clickableSubmitButtonTest() {
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(matches(isClickable()));
    }

    /**
     * performs and checks valid review input
     */
    @Test
    public void validReviewInputTest() {
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewFirstName)).perform(typeText("Jan"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewLastName)).perform(typeText("Doe"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewEditText)).perform(typeText("Nothing to see here"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(click());
        intended(hasComponent(ReviewOverviewActivity.class.getName()));
    }
}