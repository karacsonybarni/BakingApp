package com.udacity.bakingapp.ui.descriptionview;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class DescriptionAdapter
        extends RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder> {

    private static final int VIEW_TYPE_INGREDIENTS = 0;
    private static final int VIEW_TYPE_STEP = 1;


    private Context context;
    private Recipe recipe;
    private ProgressiveMediaSource.Factory mediaSourceFactory;
    private List<SimpleExoPlayer> exoPlayers;

    DescriptionAdapter(Context context) {
        this.context = context;
        exoPlayers = new ArrayList<>();
    }

    void update(Recipe recipe) {
        this.recipe = recipe;
        if (recipe != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_INGREDIENTS;
        } else {
            return VIEW_TYPE_STEP;
        }
    }

    @NonNull
    @Override
    public DescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdByViewType(viewType);
        View layout = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new DescriptionViewHolder(layout, viewType);
    }

    private int getLayoutIdByViewType(int viewType) {
        if (viewType == 0) {
            return R.layout.ingredients;
        } else {
            return R.layout.step;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_INGREDIENTS) {
            fillIngredientsLayout(holder);
        } else {
            fillStepLayout(holder, position);
        }
    }

    private void fillIngredientsLayout(DescriptionViewHolder holder) {
        ViewGroup ingredientsLayout = holder.ingredientsLayout;
        for (Ingredient ingredient : recipe.getIngredients()) {
            TextView ingredientView =
                    (TextView) LayoutInflater
                            .from(context)
                            .inflate(R.layout.ingredient, ingredientsLayout, false);
            ingredientView.setText(getIngredientText(ingredient));
            ingredientsLayout.addView(ingredientView);
        }
    }

    private String getIngredientText(Ingredient ingredient) {
        String quantity = new DecimalFormat("#.##").format(ingredient.getQuantity());
        return context.getString(
                R.string.ingredient,
                quantity, ingredient.getMeasure(), ingredient.getName());
    }

    private void fillStepLayout(DescriptionViewHolder holder, int position) {
        int stepIndex = position - 1;
        Step step = recipe.getSteps().get(stepIndex);
        holder.shortDescription.setText(step.getShortDescription());
        holder.description.setText(step.getDescription());
        showVideoOrThumbnail(holder, step);
    }

    private void showVideoOrThumbnail(DescriptionViewHolder holder, Step step) {
        String videoUrl = step.getVideoURL();
        if (!videoUrl.isEmpty()) {
            initPlayerView(holder, videoUrl);
            return;
        }

        String thumbnailUrl = step.getThumbnailURL();
        if (!thumbnailUrl.isEmpty() && isImageUrl(thumbnailUrl)) {
            initThumbnail(holder, step);
        }
    }

    private void initPlayerView(DescriptionViewHolder holder, String videoUrl) {
        PlayerView playerView = holder.playerView;
        SimpleExoPlayer exoPlayer = holder.exoPlayer;
        playerView.setPlayer(exoPlayer);
        MediaSource mediaSource = newProgressiveMediaSource(videoUrl);
        exoPlayer.prepare(mediaSource);
        playerView.setVisibility(View.VISIBLE);
    }

    private MediaSource newProgressiveMediaSource(String urlString) {
        Uri uri = Uri.parse(urlString);
        return getMediaSourceFactory().createMediaSource(uri);
    }

    private ProgressiveMediaSource.Factory getMediaSourceFactory() {
        if (mediaSourceFactory == null) {
            String userAgent = Util.getUserAgent(context, "BakingApp");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            mediaSourceFactory =
                    new ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory);
        }
        return mediaSourceFactory;
    }

    private boolean isImageUrl(String url) {
        // Dummy image url validator
        return !url.endsWith(".mp4");
    }

    private void initThumbnail(DescriptionViewHolder holder, Step step) {
        ImageView imageView = holder.thumbnail;
        Picasso.get().load(step.getThumbnailURL()).into(imageView);
        imageView.setContentDescription(step.getShortDescription());
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return recipe != null ? recipe.getSteps().size() + 1 : 0;
    }

    void close() {
        releasePlayers();
        context = null;
    }

    private void releasePlayers() {
        for (SimpleExoPlayer exoPlayer : exoPlayers) {
            exoPlayer.stop();
            exoPlayer.release();
        }
        exoPlayers = null;
    }

    class DescriptionViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup ingredientsLayout;

        private TextView shortDescription;
        private TextView description;
        private PlayerView playerView;
        private SimpleExoPlayer exoPlayer;
        private ImageView thumbnail;

        DescriptionViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_INGREDIENTS) {
                initIngredientsLayout();
            } else {
                initStepLayout();
            }
        }

        private void initIngredientsLayout() {
            ingredientsLayout = itemView.findViewById(R.id.ingredients);
        }

        private void initStepLayout() {
            shortDescription = itemView.findViewById(R.id.shortDescription);
            description = itemView.findViewById(R.id.description);
            playerView = itemView.findViewById(R.id.playerView);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
            exoPlayers.add(exoPlayer);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
