package com.udacity.bakingapp.ui.stepview;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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

    public static final String RECIPE_ID_EXTRA = "recipeId";
    public static final String STEP_POSITION_EXTRA = "stepPosition";

    private RecipeViewModel viewModel;
    private Recipe recipe;
    private int stepPosition;
    private Step step;
    private ExoPlayer exoPlayer;
    private ConstraintLayout rootLayout;
    private PlayerView playerView;
    private ImageView thumbnailView;
    private TextView descriptionView;
    private MaterialButton prevButton;
    private MaterialButton nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        initViews();
        initPlayer();
        viewModel = RecipeViewModelFactory.getViewModel(this, getRecipeId());
        stepPosition = getStepPosition();
        updateRecipe();
    }

    private void initViews() {
        rootLayout = findViewById(R.id.stepLayout);
        playerView = findViewById(R.id.playerView);
        thumbnailView = findViewById(R.id.thumbnail);
        descriptionView = findViewById(R.id.description);
        prevButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void initPlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(exoPlayer);
    }

    private long getRecipeId() {
        return getIntent().getLongExtra(RECIPE_ID_EXTRA, -1);
    }

    private int getStepPosition() {
        return getIntent().getIntExtra(STEP_POSITION_EXTRA, -1);
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
        descriptionView.setText(step.getDescription());
        showVideoOrThumbnail();
        updateButtons();
    }

    private void showVideoOrThumbnail() {
        if (!step.getVideoURL().isEmpty()) {
            updatePlayerView();
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
            thumbnailView.setVisibility(View.GONE);
        }
    }

    private void updatePlayerView() {
        MediaSource mediaSource = newProgressiveMediaSource();
        exoPlayer.prepare(mediaSource);
    }

    private MediaSource newProgressiveMediaSource() {
        Uri uri = Uri.parse(step.getVideoURL());
        return newMediaSourceFactory().createMediaSource(uri);
    }

    private ProgressiveMediaSource.Factory newMediaSourceFactory() {
        String userAgent = Util.getUserAgent(this, "BakingApp");
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, userAgent);
        ExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        return new ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory);
    }

    private void showPlayerView() {
        playerView.setVisibility(View.VISIBLE);
        alignDescriptionUnder(R.id.playerView);
    }

    private void alignDescriptionUnder(int mediaViewId) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootLayout);
        constraintSet.connect(
                R.id.description, ConstraintSet.TOP,
                mediaViewId, ConstraintSet.BOTTOM);
        constraintSet.applyTo(rootLayout);
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

    private void updateButtons() {
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

    @Override
    protected void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
        super.onDestroy();
    }
}
