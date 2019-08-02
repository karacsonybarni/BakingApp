package com.udacity.bakingapp.ui.steplistview;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import com.udacity.bakingapp.TestData;
import com.udacity.bakingapp.data.entity.Recipe;

import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class StepListActivityTest {

    private static Map<Long, Recipe> recipeMap = TestData.getRecipeMap();

    @Rule
    public ActivityTestRule<StepListActivity> activityRule =
            new ActivityTestRule<StepListActivity>(StepListActivity.class) {
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
}