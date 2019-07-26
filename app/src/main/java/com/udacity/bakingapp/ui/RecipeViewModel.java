package com.udacity.bakingapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;

public class RecipeViewModel extends ViewModel {

    private LiveData<Recipe> recipe;

    RecipeViewModel(Repository repository, long id) {
        recipe = repository.getRecipe(id);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}
