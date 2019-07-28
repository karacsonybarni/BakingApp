package com.udacity.bakingapp.ui.descriptionview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.ui.RecipeViewModel;
import com.udacity.bakingapp.ui.RecipeViewModelFactory;

import java.util.Objects;

public class DescriptionActivity extends AppCompatActivity {

    public static final String RECIPE_ID_EXTRA = "recipeId";

    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = RecipeViewModelFactory.getViewModel(this, getRecipeId());
        setContentView(R.layout.activity_description);
        updateRecipe();
    }

    private long getRecipeId() {
        return getIntent().getLongExtra(RECIPE_ID_EXTRA, -1);
    }

    private void updateRecipe() {
        viewModel.getRecipe().observe(this, this::updateRecipe);
    }

    private void updateRecipe(Recipe recipe) {
        setTitle(recipe.getName());
    }

    private void setTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    protected void onDestroy() {
        viewModel.getRecipe().removeObservers(this);
        super.onDestroy();
    }
}
