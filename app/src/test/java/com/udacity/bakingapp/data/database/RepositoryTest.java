package com.udacity.bakingapp.data.database;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.udacity.bakingapp.data.database.entity.Recipe;
import com.udacity.bakingapp.data.network.RecipesNetworkDataSource;
import com.udacity.bakingapp.util.AppExecutors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private CountDownLatch latch = new CountDownLatch(1);
    private RecipesNetworkDataSource recipesNetworkDataSource;

    @Before
    public void setup() {
        recipesNetworkDataSource = new RecipesNetworkDataSource();
        setupRepository();
    }

    private void setupRepository() {
        new Repository(recipesNetworkDataSource, AppExecutors.getInstance()) {
            @Override
            void updateRecipes(List<Recipe> recipes) {
                assertThat(recipes).isNotEmpty();
                latch.countDown();
            }
        };
    }

    @Test
    public void updateRecipes() throws InterruptedException {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe());
        recipesNetworkDataSource.setRecipes(recipes);
        latch.await();
    }
}