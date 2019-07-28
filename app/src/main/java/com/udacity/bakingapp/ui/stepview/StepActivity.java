package com.udacity.bakingapp.ui.stepview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.ui.RecipeViewModel;
import com.udacity.bakingapp.ui.RecipeViewModelFactory;

import java.util.List;
import java.util.Objects;

public class StepActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipeId";
    public static final String STEP_POSITION = "stepPosition";

    private RecipeViewModel viewModel;
    private Recipe recipe;
    private StepFragment stepFragment;
    private int stepPosition;

    @Nullable
    private MaterialButton prevButton;
    @Nullable
    private MaterialButton nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = RecipeViewModelFactory.getViewModel(this, getRecipeId());
        stepPosition = getStepPosition(savedInstanceState);
        setContentView(R.layout.activity_step);
        initViews();
        initStepFragment();
        enterFullscreenIfInLandscape();
        updateRecipe();
    }

    private long getRecipeId() {
        return getIntent().getLongExtra(RECIPE_ID, -1);
    }

    private int getStepPosition(Bundle savedInstanceState) {
        int stepPosition = -1;
        if (savedInstanceState != null) {
            stepPosition = savedInstanceState.getInt(STEP_POSITION, -1);
        }
        if (stepPosition == -1) {
            stepPosition = getIntent().getIntExtra(STEP_POSITION, -1);
        }
        return stepPosition;
    }

    private void initViews() {
        prevButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void initStepFragment() {
        stepFragment =
                (StepFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.stepFragmentContainer);
        if (stepFragment == null) {
            stepFragment = new StepFragment();
            stepFragment.setStepPosition(stepPosition);
            addStepFragment();
        } else {
            stepFragment.setStepPosition(stepPosition);
        }
    }

    private void addStepFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.stepFragmentContainer, stepFragment)
                .commit();
    }

    private void enterFullscreenIfInLandscape() {
        if (isInLandscapeMode()) {
            hideActionBar();
            updateSystemUiVisibilityFlags();
        }
    }

    private boolean isInLandscapeMode() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void updateSystemUiVisibilityFlags() {
        getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void updateRecipe() {
        viewModel.getRecipe().observe(this, this::updateRecipe);
    }

    private void updateRecipe(Recipe recipe) {
        this.recipe = recipe;
        setTitle();
        updateButtons();
    }

    private void setTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());
    }

    private void updateButtons() {
        if (prevButton == null || nextButton == null) {
            return;
        }
        List<Step> steps = recipe.getSteps();
        if (stepPosition == 0) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setOnClickListener(getPrevButtonClickListener());
            prevButton.setEnabled(true);
        }
        if (stepPosition == steps.size() - 1) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setOnClickListener(getNextButtonClickListener());
            nextButton.setEnabled(true);
        }
    }

    private View.OnClickListener getPrevButtonClickListener() {
        return view -> {
            stepPosition--;
            updateStep();
        };
    }

    private View.OnClickListener getNextButtonClickListener() {
        return view -> {
            stepPosition++;
            updateStep();
        };
    }

    private void updateStep() {
        stepFragment.setStepPosition(stepPosition);
        stepFragment.updateViews();
        updateButtons();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaProvider.stopPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_POSITION, stepPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getRecipe().removeObservers(this);
    }
}
