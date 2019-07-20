package com.udacity.bakingapp.ui.descriptionview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;

class DescriptionActivityViewModel extends ViewModel {

    private LiveData<Recipe> recipe;

    DescriptionActivityViewModel(Repository repository, long id) {
        recipe = repository.getRecipe(id);
    }

    LiveData<Recipe> getRecipe() {
        return recipe;
    }
}
