package com.udacity.bakingapp.ui.stepview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.ui.RecipeViewModel;
import com.udacity.bakingapp.util.ConfigurationUtils;

import java.util.Objects;

public class StepFragment extends Fragment {

    private RecipeViewModel viewModel;
    private Recipe recipe;
    private int stepPosition;
    private Step step;
    private PlayerView playerView;
    private ImageView thumbnailView;

    private ViewGroup rootView;
    @Nullable
    private TextView descriptionView;

    @Nullable
    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        rootView = inflateView(inflater, container);
        initViews();
        initPlayer();
        updateRecipe();
        return rootView;
    }

    private RecipeViewModel getViewModel() {
        return ViewModelProviders.of(getNonNullActivity()).get(RecipeViewModel.class);
    }

    @NonNull
    private Context getNonNullContext() {
        return Objects.requireNonNull(getContext());
    }

    @NonNull
    private FragmentActivity getNonNullActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private ViewGroup inflateView(LayoutInflater inflater, ViewGroup container) {
        return (ViewGroup) inflater
                .inflate(R.layout.fragment_step, container, false);
    }

    private void initViews() {
        playerView = rootView.findViewById(R.id.playerView);
        thumbnailView = rootView.findViewById(R.id.thumbnail);
        descriptionView = rootView.findViewById(R.id.description);
    }

    private void initPlayer() {
        ExoPlayer exoPlayer = MediaProvider.getExoPlayer(getContext());
        playerView.setPlayer(exoPlayer);
    }

    private void updateRecipe() {
        viewModel.getRecipe().observe(this, this::updateRecipe);
    }

    private void updateRecipe(Recipe recipe) {
        this.recipe = recipe;
        updateStep();
    }

    private void updateStep() {
        step = recipe.getSteps().get(stepPosition);
        populateViews();
    }

    private void populateViews() {
        fillDescription();
        showVideoOrThumbnail();
    }

    private void fillDescription() {
        if (descriptionView != null) {
            descriptionView.setText(step.getDescription());
        }
    }

    private void showVideoOrThumbnail() {
        String videoUrl = step.getVideoURL();
        if (!videoUrl.isEmpty()) {
            MediaProvider.updateMediaSource(getContext(), videoUrl);
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
            if (shouldShowThumbnailError()) {
                showThumbnailError();
            } else {
                thumbnailView.setVisibility(View.GONE);
            }
        }
    }

    private void showPlayerView() {
        thumbnailView.setVisibility(View.GONE);
        playerView.setVisibility(View.VISIBLE);
        alignDescriptionUnder(R.id.playerView);
    }

    private void alignDescriptionUnder(int mediaViewId) {
        ConstraintLayout constraintLayout = getConstraintLayout();
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

    @Nullable
    private ConstraintLayout getConstraintLayout() {
        return getNonNullActivity().findViewById(R.id.stepLayout);
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
        playerView.setVisibility(View.GONE);
        thumbnailView.setVisibility(View.VISIBLE);
        alignDescriptionUnder(R.id.thumbnail);
    }

    private boolean shouldShowThumbnailError() {
        Context context = getNonNullContext();
        return !ConfigurationUtils.isTablet(context)
                && ConfigurationUtils.isInLandscapeMode(context);
    }

    private void showThumbnailError() {
        thumbnailView.setImageResource(R.drawable.ic_broken_image_black_500);
        thumbnailView.setVisibility(View.VISIBLE);
    }

    public void setStepPosition(int stepPosition) {
        this.stepPosition = stepPosition;
    }

    public void updateViews() {
        MediaProvider.stopPlayer();
        updateStep();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.getRecipe().removeObservers(this);
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            MediaProvider.close();
        }
    }
}
