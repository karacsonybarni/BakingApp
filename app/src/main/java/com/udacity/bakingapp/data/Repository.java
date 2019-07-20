package com.udacity.bakingapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.udacity.bakingapp.data.database.Database;
import com.udacity.bakingapp.data.database.RecipeDao;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.network.RecipesNetworkDataSource;
import com.udacity.bakingapp.util.AppExecutors;

import java.util.List;

public class Repository {

    private static Repository sInstance;
    private RecipeDao recipeDao;
    private AppExecutors executors;

    Repository(
            Database database,
            RecipesNetworkDataSource recipesNetworkDataSource,
            AppExecutors executors) {
        recipeDao = database.recipeDao();
        this.executors = executors;

        recipesNetworkDataSource.getRecipes().observeForever(newNetworkDataObserver());
        recipesNetworkDataSource.fetchRecipes();
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
        recipeDao.updateRecipes(recipes);
    }

    public static Repository getInstance(
            Database database,
            RecipesNetworkDataSource recipesNetworkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            sInstance = new Repository(database, recipesNetworkDataSource, executors);
        }
        return sInstance;
    }

    public LiveData<Recipe> getRecipe(long id) {
        return recipeDao.getRecipe(id);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeDao.getRecipes();
    }
}
