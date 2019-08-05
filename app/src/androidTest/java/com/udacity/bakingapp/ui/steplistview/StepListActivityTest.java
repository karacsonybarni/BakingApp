package com.udacity.bakingapp.ui.steplistview;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.TestData;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.ui.stepview.StepActivity;
import com.udacity.bakingapp.util.ConfigurationUtils;

import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class StepListActivityTest {

    private static Map<Long, Recipe> recipeMap = TestData.getRecipeMap();

    @Rule
    public IntentsTestRule<StepListActivity> activityRule =
            new IntentsTestRule<StepListActivity>(StepListActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context context = ApplicationProvider.getApplicationContext();
                    Intent intent = new Intent(context, StepListActivity.class);
                    Recipe nutellaPie = recipeMap.get(TestData.NUTELLA_PIE_ID);
                    intent.putExtra(StepListActivity.RECIPE_ID_EXTRA, nutellaPie.getId());
                    return intent;
                }
            };

    @Test
    public void testTitle() {
        String recipeName = getRecipeNameByIdFromIntent();
        onView(withText(recipeName)).check(matches(isDisplayed()));
    }

    private String getRecipeNameByIdFromIntent() {
        Intent intent = activityRule.getActivity().getIntent();
        long recipeId = intent.getLongExtra(StepListActivity.RECIPE_ID_EXTRA, -1);
        return recipeMap.get(recipeId).getName();
    }

    @Test
    public void testIntentAndStepDescriptionVisibility() {
        Recipe nutellaPie = recipeMap.get(TestData.NUTELLA_PIE_ID);
        int stepPosition = 0;
        testIntent(nutellaPie, stepPosition);
        testStepDescriptionIsDisplayed(nutellaPie, stepPosition);
    }

    private void testIntent(Recipe recipe, int stepPosition) {
        Step step = recipe.getSteps().get(stepPosition);
        onView(withText(step.getShortDescription())).perform(click());
        if (!isTablet()) {
            intended(allOf(
                    hasExtra(StepActivity.RECIPE_ID, recipe.getId()),
                    hasExtra(StepActivity.STEP_POSITION, stepPosition),
                    toPackage("com.udacity.bakingapp")
            ));
        }
    }

    private boolean isTablet() {
        return ConfigurationUtils.isTablet(activityRule.getActivity());
    }

    private void testStepDescriptionIsDisplayed(Recipe recipe, int stepPosition) {
        Step step = recipe.getSteps().get(stepPosition);
        onView(withText(step.getDescription())).check(matches(isDisplayed()));
    }

    @Test
    public void testTwoPaneLayout() {
        if (isTablet()) {
            onView(withId(R.id.stepFragmentContainer)).check(matches(isDisplayed()));
        }
    }
}