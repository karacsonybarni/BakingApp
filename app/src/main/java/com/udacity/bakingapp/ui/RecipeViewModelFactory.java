package com.udacity.bakingapp.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.util.InjectorUtils;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;
    private long id;

    private RecipeViewModelFactory(Repository repository, long id) {
        this.repository = repository;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeViewModel(repository, id);
    }

    public static RecipeViewModel getViewModel(FragmentActivity activity, long id) {
        Repository repository = InjectorUtils.getRepository(activity);
        RecipeViewModelFactory factory = new RecipeViewModelFactory(repository, id);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(activity, factory);
        return viewModelProvider.get(RecipeViewModel.class);
    }
}
