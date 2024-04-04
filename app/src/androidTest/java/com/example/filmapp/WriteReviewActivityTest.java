package com.example.filmapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.filmapp.R;
import com.example.filmapp.activities.WriteReviewActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test will check if write review activity launches successfully
 *
 */
@RunWith(AndroidJUnit4.class)
public class WriteReviewActivityTest {

    @Rule
    public IntentsTestRule<WriteReviewActivity> intentsTestRule = new IntentsTestRule<>(WriteReviewActivity.class);

    @Test
    public void testLaunch() {
        Espresso.onView(ViewMatchers.withId(R.id.writeReviewFirstName))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
