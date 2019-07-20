package com.udacity.bakingapp.ui.recipesview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;

import java.util.List;

class RecipesActivityViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipes;

    RecipesActivityViewModel(Repository repository) {
        recipes = repository.getRecipes();
    }

    LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
