package com.example.filmapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.example.filmapp.activities.ReviewOverviewActivity;
import com.example.filmapp.activities.WriteReviewActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Test will check if Review Overview will open correct activity for adding new review
 *
 */
@RunWith(JUnit4.class)
@LargeTest
public class SwitchWriteNewReviewTest {

    @Rule
    public ActivityScenarioRule<ReviewOverviewActivity> activityRule =
            new ActivityScenarioRule<>(ReviewOverviewActivity.class);

    @Test
    public void switchToWriteReviewTest() {
        // clicks on the fab to write nerw review
        Espresso.onView(ViewMatchers.withId(R.id.fab))
                .perform(ViewActions.click());

        // checks if correct activity opened
        Espresso.onView(ViewMatchers.withId(R.id.button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}