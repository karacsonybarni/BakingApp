package com.udacity.bakingapp.ui.recipesview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.bakingapp.data.Repository;

class RecipesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;

    RecipesViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipesActivityViewModel(repository);
    }
}
