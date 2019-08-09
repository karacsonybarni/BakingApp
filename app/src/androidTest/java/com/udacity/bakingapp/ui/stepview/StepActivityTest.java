package com.udacity.bakingapp.ui.stepview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.udacity.bakingapp.BaseTest;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.TestData;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.util.ConfigurationUtils;

import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class StepActivityTest extends BaseTest {

    private static Map<Long, Recipe> recipeMap = TestData.getRecipeMap();
    private Recipe nutellaPie = recipeMap.get(TestData.NUTELLA_PIE_ID);
    private int stepPosition = 0;

    @Rule
    public IntentsTestRule<StepActivity> activityRule =
            new IntentsTestRule<StepActivity>(StepActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context context = ApplicationProvider.getApplicationContext();
                    Intent intent = new Intent(context, StepActivity.class);
                    intent.putExtra(StepActivity.RECIPE_ID, nutellaPie.getId());
                    intent.putExtra(StepActivity.STEP_POSITION, stepPosition);
                    return intent;
                }
            };

    @Test
    @PhoneTest
    public void testFullScreenVideo() {
        activityRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.playerView)).check(matches(isDisplayed()));
        onView(withId(R.id.description)).check(doesNotExist());
    }

    @Test
    @PhoneTest
    public void testNextButtonAndErrorImage() {
        testNextButton();
        testErrorImage();
    }

    private void testNextButton() {
        onView(withText(R.string.next_button)).perform(click());
        Step nextStep = nutellaPie.getSteps().get(stepPosition + 1);
        onView(withText(nextStep.getDescription())).check(matches(isDisplayed()));
    }

    private void testErrorImage() {
        activityRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.thumbnail)).check(matches(isDisplayed()));
    }

    @Override
    protected boolean isTablet() {
        return ConfigurationUtils.isTablet(activityRule.getActivity());
    }

    @Override
    protected boolean isPhone() {
        return !isTablet();
    }
}