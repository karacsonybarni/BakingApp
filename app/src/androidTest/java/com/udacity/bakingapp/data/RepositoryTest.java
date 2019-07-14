package com.udacity.bakingapp.data;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import com.udacity.bakingapp.data.database.Database;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.network.RecipesNetworkDataSource;
import com.udacity.bakingapp.ui.recipesview.RecipesActivity;
import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.util.InjectorUtils;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTest {

    @Rule
    public ActivityTestRule<RecipesActivity> activityActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private Executor mainThreadExecutor = AppExecutors.getInstance().mainThread();

    @Test
    public void updateRecipes() throws InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        mainThreadExecutor.execute(() -> stubRepository(context));
        latch.await();
    }

    private void stubRepository(Context context) {
        Database database =
                Database.getInstance(context);
        RecipesNetworkDataSource recipesNetworkDataSource =
                RecipesNetworkDataSource.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        new Repository(database, recipesNetworkDataSource, executors) {
            @Override
            void updateRecipes(List<Recipe> recipes) {
                assertThat(recipes).isNotEmpty();
                latch.countDown();
            }
        };
    }

    @Test
    public void getRecipes() throws InterruptedException {
        CountDownLatch dbLatch = new CountDownLatch(2);
        mainThreadExecutor.execute(() -> observeRecipes(dbLatch));
        dbLatch.await();
    }

    private void observeRecipes(CountDownLatch dbLatch) {
        Context context = ApplicationProvider.getApplicationContext();
        Repository repository = InjectorUtils.getRepository(context);
        repository.getRecipes().observe(activityActivityTestRule.getActivity(),
                recipes -> {
                    if (dbLatch.getCount() == 1 || !recipes.isEmpty()) {
                        testRecipes(recipes);
                        dbLatch.countDown();
                    }

                    dbLatch.countDown();
                });
    }

    private void testRecipes(List<Recipe> recipes) {
        assertThat(recipes).isNotEmpty();
        String ingredientName = recipes.get(0).getIngredients().get(0).getName();
        assertThat(ingredientName).isEqualTo("Graham Cracker crumbs");
    }
}