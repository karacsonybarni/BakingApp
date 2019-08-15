package com.udacity.bakingapp.ui.recipesview;

import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.network.RecipesNetworkDataSource;
import com.udacity.bakingapp.util.ConfigurationUtils;
import com.udacity.bakingapp.util.InjectorUtils;

import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private RecipesActivityViewModel viewModel;
    private RecipesAdapter adapter;
    private RecyclerView recyclerView;
    private SimpleIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        viewModel = getViewModel();
        adapter = new RecipesAdapter(this);
        idlingResource = getIdlingResource();
        observeRecipes();
        initRecyclerView();
    }

    private RecipesActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        RecipesViewModelFactory factory = new RecipesViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(RecipesActivityViewModel.class);
    }

    @VisibleForTesting
    SimpleIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    private void observeRecipes() {
        idlingResource.setIdleState(false);
        viewModel.getRecipes().observe(this, this::updateRecipes);
    }

    private void updateRecipes(List<Recipe> recipes) {
        if (recipes.size() > 0) {
            adapter.updateAll(recipes);
        } else {
            Snackbar
                    .make(recyclerView, R.string.no_internet, Snackbar.LENGTH_LONG)
                    .setAction(
                            R.string.retry,
                            v -> RecipesNetworkDataSource.getInstance(this).fetchRecipes())
                    .show();
        }
        idlingResource.setIdleState(true);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recipes);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(adapter);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        if (ConfigurationUtils.isTablet(this)) {
            return new GridLayoutManager(this, 3);
        } else {
            return new LinearLayoutManager(this);
        }
    }

    @Override
    protected void onDestroy() {
        viewModel.getRecipes().removeObservers(this);
        super.onDestroy();
    }
}
