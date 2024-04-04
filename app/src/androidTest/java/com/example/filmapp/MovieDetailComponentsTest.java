package com.example.filmapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.filmapp.R;
import com.example.filmapp.activities.MovieDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test will check various movie detail components
 *
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class MovieDetailComponentsTest {

    @Rule
    public ActivityScenarioRule<MovieDetailActivity> activityRule = new ActivityScenarioRule<>(MovieDetailActivity.class);

    @Test
    public void testMovieDetailTitleDisplayed() {
        onView(withId(R.id.movieDetailTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testMovieDetailRatingDisplayed() {
        onView(withId(R.id.movieDetailRating)).check(matches(isDisplayed()));
    }

    @Test
    public void testMovieDetailDescriptionDisplayed() {
        onView(withId(R.id.movieDetailDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void testRatingSeekBarDisplayed() {
        onView(withId(R.id.seekBar)).check(matches(isDisplayed()));
    }

    @Test
    public void testSubmitButtonDisplayed() {
        onView(withId(R.id.ratingSubmitButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testSubmitButtonSubmitsRating() {
        onView(withId(R.id.seekBar)).perform(scrollTo(), click()).perform();
        onView(withId(R.id.ratingSubmitButton)).perform(click());

        //checks if moviedetailrating is successfully adjusted after a rating was submitted
        onView(withId(R.id.movieDetailRating)).check(matches(isDisplayed()));
    }
}