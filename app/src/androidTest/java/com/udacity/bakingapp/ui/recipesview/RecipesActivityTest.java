package com.udacity.bakingapp.ui.recipesview;

import androidx.test.rule.ActivityTestRule;

import com.udacity.bakingapp.TestData;
import com.udacity.bakingapp.data.database.entity.Recipe;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class RecipesActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void testCardsAreDisplayed() {
        List<Recipe> recipes = TestData.getRecipes();
        assert recipes != null;
        for (Recipe recipe : recipes) {
            onView(withText(recipe.getName())).check(matches(isDisplayed()));
        }
    }

}