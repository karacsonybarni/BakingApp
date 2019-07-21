package com.udacity.bakingapp.ui.descriptionview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.InjectorUtils;

import java.util.Objects;

public class DescriptionActivity extends AppCompatActivity {

    public static final String RECIPE_ID_EXTRA = "recipeId";

    private DescriptionActivityViewModel viewModel;
    private DescriptionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        viewModel = getViewModel(getRecipeId());
        adapter = new DescriptionAdapter(this);
        updateRecipe();
        initRecyclerView();
    }

    private long getRecipeId() {
        return Objects.requireNonNull(getIntent().getExtras()).getLong(RECIPE_ID_EXTRA);
    }

    private DescriptionActivityViewModel getViewModel(long id) {
        Repository repository = InjectorUtils.getRepository(this);
        DescriptionViewModelFactory factory = new DescriptionViewModelFactory(repository, id);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(DescriptionActivityViewModel.class);
    }

    private void updateRecipe() {
        viewModel.getRecipe().observe(this, this::updateRecipe);
    }

    private void updateRecipe(Recipe recipe) {
        setTitle(recipe.getName());
        adapter.update(recipe);
    }

    private void setTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.description_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        viewModel.getRecipe().removeObservers(this);
        adapter.close();
        super.onDestroy();
    }
}
