package com.udacity.bakingapp.ui.stepview;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

class MediaProvider {

    private static MediaProvider INSTANCE;

    private ExoPlayer exoPlayer;
    private String mediaSourceUrl;

    private static MediaProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MediaProvider();
        }
        return INSTANCE;
    }

    static ExoPlayer getExoPlayer(Context context) {
        MediaProvider mediaProviderInstance = getInstance();
        ExoPlayer exoPlayer = mediaProviderInstance.getExoPlayer();
        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
            mediaProviderInstance.setExoPlayer(exoPlayer);
        }
        return exoPlayer;
    }

    private ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    private void setExoPlayer(ExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    static void updateMediaSource(Context context, String mediaSourceUrl) {
        MediaProvider mediaProviderInstance = getInstance();
        if (!mediaSourceUrl.equals(mediaProviderInstance.getMediaSourceUrl())) {
            mediaProviderInstance.setMediaSourceUrl(mediaSourceUrl);
            MediaSource mediaSource = mediaProviderInstance.newProgressiveMediaSource(context);
            getExoPlayer(context).prepare(mediaSource);
        }
    }

    private String getMediaSourceUrl() {
        return mediaSourceUrl;
    }

    private void setMediaSourceUrl(String mediaSourceUrl) {
        this.mediaSourceUrl = mediaSourceUrl;
    }

    private MediaSource newProgressiveMediaSource(Context context) {
        Uri uri = Uri.parse(getInstance().getMediaSourceUrl());
        return newMediaSourceFactory(context).createMediaSource(uri);
    }

    private ProgressiveMediaSource.Factory newMediaSourceFactory(Context context) {
        String userAgent = Util.getUserAgent(context, "BakingApp");
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, userAgent);
        ExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        return new ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory);
    }

    static void stopPlayer() {
        ExoPlayer exoPlayer = getInstance().getExoPlayer();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    static void close() {
        MediaProvider mediaProviderInstance = getInstance();
        ExoPlayer exoPlayer = mediaProviderInstance.getExoPlayer();
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            mediaProviderInstance.setExoPlayer(null);
            mediaProviderInstance.setMediaSourceUrl(null);
        }
    }
}
