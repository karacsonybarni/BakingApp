package com.udacity.bakingapp.ui.descriptionview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.ui.RecipeViewModel;
import com.udacity.bakingapp.ui.RecipeViewModelFactory;

import java.util.Objects;

public class DescriptionActivity extends AppCompatActivity {

    public static final String RECIPE_ID_EXTRA = "recipeId";

    private RecipeViewModel viewModel;
    private DescriptionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        viewModel = RecipeViewModelFactory.getViewModel(this, getRecipeId());
        adapter = new DescriptionAdapter(this);
        updateRecipe();
        initRecyclerView();
    }

    private long getRecipeId() {
        return getIntent().getLongExtra(RECIPE_ID_EXTRA, -1);
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
        DividerItemDecoration divider =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        viewModel.getRecipe().removeObservers(this);
        super.onDestroy();
    }
}
