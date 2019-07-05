package com.udacity.bakingapp.ui.recipesview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.database.entity.Recipe;
import com.udacity.bakingapp.util.InjectorUtils;

import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private RecipesActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = getViewModel();
        observeRecipes();
    }

    private RecipesActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        RecipesViewModelFactory factory = new RecipesViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(RecipesActivityViewModel.class);
    }

    private void observeRecipes() {
        viewModel.getRecipes().observe(this, this::updateRecipes);
    }

    private void updateRecipes(List<Recipe> recipes) {

    }

    @Override
    protected void onDestroy() {
        viewModel.getRecipes().removeObservers(this);
        super.onDestroy();
    }
}
