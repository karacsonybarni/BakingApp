package com.udacity.bakingapp.ui.steplistview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.ui.RecipeViewModel;
import com.udacity.bakingapp.ui.RecipeViewModelFactory;
import com.udacity.bakingapp.ui.stepview.StepActivity;
import com.udacity.bakingapp.util.ConfigurationUtils;

import java.util.Objects;

public class StepListActivity extends AppCompatActivity implements OnSelectionListener {

    public static final String RECIPE_ID_EXTRA = "recipeId";

    private long recipeId;
    private RecipeViewModel viewModel;

    @Nullable
    private TwoPaneStepListActivityDelegate twoPaneDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeId = getRecipeId();
        viewModel = RecipeViewModelFactory.getViewModel(this, recipeId);
        setContentView(R.layout.activity_step_list);
        updateRecipe();
        setupFragmentsIfTablet(savedInstanceState);
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

    private void setupFragmentsIfTablet(Bundle savedInstanceState) {
        if (ConfigurationUtils.isTablet(this)) {
            twoPaneDelegate = new TwoPaneStepListActivityDelegate(this);
            twoPaneDelegate.setup(savedInstanceState);
        }
    }

    @Override
    public void onSelect(int stepPosition) {
        if (twoPaneDelegate != null) {
            twoPaneDelegate.onSelect(stepPosition);
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(StepActivity.RECIPE_ID, recipeId);
            intent.putExtra(StepActivity.STEP_POSITION, stepPosition);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (twoPaneDelegate != null) {
            twoPaneDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        viewModel.getRecipe().removeObservers(this);
        super.onDestroy();
    }
}
