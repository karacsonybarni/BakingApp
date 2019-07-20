package com.udacity.bakingapp.ui.descriptionview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.InjectorUtils;

import java.util.Objects;

public class DescriptionActivity extends AppCompatActivity {

    public static final String RECIPE_ID_EXTRA = "recipeId";

    private DescriptionActivityViewModel viewModel;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        viewModel = getViewModel(getRecipeId());
        populateViews();
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

    private void populateViews() {
        viewModel.getRecipe().observe(this, this::populateViews);
    }

    private void populateViews(Recipe recipe) {
        this.recipe = recipe;
        setTitle();
    }

    private void setTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());
    }
}
