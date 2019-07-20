package com.udacity.bakingapp.ui.descriptionview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.bakingapp.data.Repository;

class DescriptionViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;
    private long id;

    DescriptionViewModelFactory(Repository repository, long id) {
        this.repository = repository;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DescriptionActivityViewModel(repository, id);
    }
}
