package com.udacity.bakingapp.data.database;

import androidx.lifecycle.Observer;

import com.udacity.bakingapp.data.database.entity.Recipe;
import com.udacity.bakingapp.data.network.RecipesNetworkDataSource;
import com.udacity.bakingapp.util.AppExecutors;

import java.util.List;

public class Repository {

    private static Repository sInstance;
    private RecipesNetworkDataSource recipesNetworkDataSource;
    private AppExecutors executors;
    private Observer<? super List<Recipe>> networkDataObserver;

    Repository(
            RecipesNetworkDataSource recipesNetworkDataSource,
            AppExecutors executors) {
        this.recipesNetworkDataSource = recipesNetworkDataSource;
        this.executors = executors;

        networkDataObserver = newNetworkDataObserver();
        recipesNetworkDataSource.getRecipes().observeForever(networkDataObserver);
    }

    private Observer<? super List<Recipe>> newNetworkDataObserver() {
        return recipes ->
                executors.diskIO().execute(() -> {
                    if (recipes != null && !recipes.isEmpty()) {
                        updateRecipes(recipes);
                    }
                });
    }

    void updateRecipes(List<Recipe> recipes) {

    }

    public static Repository getInstance(
            RecipesNetworkDataSource recipesNetworkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            sInstance = new Repository(recipesNetworkDataSource, executors);
        }
        return sInstance;
    }
}
