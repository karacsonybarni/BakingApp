package com.udacity.bakingapp.ui.recipesview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.ConfigurationUtils;
import com.udacity.bakingapp.util.InjectorUtils;

import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private RecipesActivityViewModel viewModel;
    private RecipesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        viewModel = getViewModel();
        adapter = new RecipesAdapter(this);
        observeRecipes();
        initRecyclerView();
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
        adapter.updateAll(recipes);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recipes);
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
