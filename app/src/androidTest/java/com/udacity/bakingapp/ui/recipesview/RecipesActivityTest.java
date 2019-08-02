package com.udacity.bakingapp.ui.recipesview;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.udacity.bakingapp.TestData;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.ui.steplistview.StepListActivity;

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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class RecipesActivityTest {

    @Rule
    public IntentsTestRule<RecipesActivity> activityActivityTestRule =
            new IntentsTestRule<>(RecipesActivity.class);

    private Map<Long, Recipe> recipeMap = TestData.getRecipeMap();

    @Test
    public void testCardsAreDisplayed() {
        for (Map.Entry<Long, Recipe> entry : recipeMap.entrySet()) {
            onView(withText(entry.getValue().getName())).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testIntent() {
        Recipe nutellaPie = recipeMap.get(TestData.NUTELLA_PIE_ID);
        onView(withText(nutellaPie.getName())).perform(click());
        intended(allOf(
                hasExtra(StepListActivity.RECIPE_ID_EXTRA, nutellaPie.getId()),
                toPackage("com.udacity.bakingapp")
        ));
    }
}