package com.udacity.bakingapp.ui.recipesview;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback resourceCallback;
    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        resourceCallback = callback;
    }

    void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && resourceCallback != null) {
            Objects.requireNonNull(resourceCallback).onTransitionToIdle();
        }
    }
}
