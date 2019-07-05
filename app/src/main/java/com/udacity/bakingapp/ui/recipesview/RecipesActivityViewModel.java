package com.udacity.bakingapp.ui.recipesview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.database.entity.Recipe;

import java.util.List;

class RecipesActivityViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Recipe>> recipes;

    RecipesActivityViewModel(Repository repository) {
        this.repository = repository;
    }

    LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = repository.getRecipes();
        }
        return recipes;
    }
}
