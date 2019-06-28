package com.udacity.bakingapp.data.network;

import androidx.lifecycle.MutableLiveData;

import com.udacity.bakingapp.data.database.entity.Recipe;

import java.util.List;

public class RecipesNetworkDataSource {

    private MutableLiveData<List<Recipe>> recipesLiveData;

    public RecipesNetworkDataSource() {
        recipesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipesLiveData;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipesLiveData.postValue(recipes);
    }
}
