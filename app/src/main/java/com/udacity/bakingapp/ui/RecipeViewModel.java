package com.udacity.bakingapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;

public class RecipeViewModel extends ViewModel {

    private Repository repository;
    private LiveData<Recipe> recipe;

    RecipeViewModel(Repository repository, long id) {
        this.repository = repository;
        recipe = repository.getRecipe(id);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public void updateLastViewed(Recipe recipe) {
        recipe.setLastViewed(System.currentTimeMillis());
        repository.updateRecipe(recipe);
    }
}
