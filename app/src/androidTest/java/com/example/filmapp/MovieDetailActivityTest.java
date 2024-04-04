package com.example.filmapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.filmapp.activities.MainActivity;
import com.example.filmapp.activities.MovieDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test will check if detail activity launches succesfully
 *
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class MovieDetailActivityTest {

    @Rule
    public ActivityScenarioRule<MovieDetailActivity> activityScenarioRule = new ActivityScenarioRule<>(MovieDetailActivity.class);

    @Test
    public void testActivityInView() {
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailGenre)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailReleaseYear)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailDuration)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailRating)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailtagline)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailDescription)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.seekBar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}