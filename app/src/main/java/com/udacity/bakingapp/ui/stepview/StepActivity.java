package com.udacity.bakingapp.ui.stepview;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
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
    private int stepPosition;
    private Step step;
    private PlayerView playerView;
    private ImageView thumbnailView;

    @Nullable
    private ConstraintLayout constraintLayout;
    @Nullable
    private TextView descriptionView;
    @Nullable
    private MaterialButton prevButton;
    @Nullable
    private MaterialButton nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        enterFullscreenIfInLandscape();
        initViews();
        initPlayer();
        viewModel = RecipeViewModelFactory.getViewModel(this, getRecipeId());
        stepPosition = getStepPosition(savedInstanceState);
        updateRecipe();
    }

    private void initViews() {
        constraintLayout = findViewById(R.id.stepLayout);
        playerView = findViewById(R.id.playerView);
        thumbnailView = findViewById(R.id.thumbnail);
        descriptionView = findViewById(R.id.description);
        prevButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
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
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            decorView
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void initPlayer() {
        ExoPlayer exoPlayer = MediaProvider.getExoPlayer(this);
        playerView.setPlayer(exoPlayer);
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

    private void updateRecipe() {
        viewModel.getRecipe().observe(this, this::updateRecipe);
    }

    private void updateRecipe(Recipe recipe) {
        this.recipe = recipe;
        setTitle();
        updateStep();
    }

    private void updateStep() {
        step = recipe.getSteps().get(stepPosition);
        populateViews();
    }

    private void setTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());
    }

    private void populateViews() {
        fillDescription();
        showVideoOrThumbnail();
        updateButtons();
    }

    private void fillDescription() {
        if (descriptionView != null) {
            descriptionView.setText(step.getDescription());
        }
    }

    private void showVideoOrThumbnail() {
        String videoUrl = step.getVideoURL();
        if (!videoUrl.isEmpty()) {
            MediaProvider.updateMediaSource(this, videoUrl);
            showPlayerView();
            return;
        } else {
            playerView.setVisibility(View.GONE);
        }

        String thumbnailUrl = step.getThumbnailURL();
        if (!thumbnailUrl.isEmpty() && isThumbnailUrlValid()) {
            updateThumbnail();
            showThumbnail();
        } else {
            if (isInLandscapeMode()) {
                showThumbnailError();
            } else {
                thumbnailView.setVisibility(View.GONE);
            }
        }
    }

    private void showPlayerView() {
        playerView.setVisibility(View.VISIBLE);
        alignDescriptionUnder(R.id.playerView);
    }

    private void alignDescriptionUnder(int mediaViewId) {
        if (constraintLayout == null) {
            return;
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(
                R.id.description, ConstraintSet.TOP,
                mediaViewId, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);
    }

    private boolean isThumbnailUrlValid() {
        // Dummy thumbnail url validator
        return !step.getThumbnailURL().endsWith(".mp4");
    }

    private void updateThumbnail() {
        Picasso.get().load(step.getThumbnailURL()).into(thumbnailView);
        thumbnailView.setContentDescription(step.getShortDescription());
    }

    private void showThumbnail() {
        thumbnailView.setVisibility(View.VISIBLE);
        alignDescriptionUnder(R.id.thumbnail);
    }

    private void showThumbnailError() {
        thumbnailView.setImageResource(R.drawable.ic_broken_image_black_500);
        thumbnailView.setVisibility(View.VISIBLE);
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
            MediaProvider.stopPlayer();
        };
    }

    private View.OnClickListener getNextButtonClickListener() {
        return view -> {
            stepPosition++;
            updateStep();
            MediaProvider.stopPlayer();
        };
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
}
