package com.udacity.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.util.InjectorUtils;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_RECIPE_NAME = "recipeName";

    @Nullable
    private String recipeName;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            onUpdateIntentReceived(intent);
        }
        super.onReceive(context, intent);
    }

    private void onUpdateIntentReceived(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(EXTRA_RECIPE_NAME)) {
            recipeName = extras.getString(EXTRA_RECIPE_NAME);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    private void updateWidget(
            Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        updateRecipe(context, views);
        appWidgetManager.updateAppWidget(widgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.ingredients);
    }

    private void updateRecipe(Context context, RemoteViews views) {
        if (recipeName != null) {
            views.setTextViewText(R.id.recipeName, recipeName);
        }
        updateIngredients(context, views);
    }

    private void updateIngredients(Context context, RemoteViews views) {
        Intent intent = new Intent(context, IngredientsRemoteViewsService.class);
        views.setRemoteAdapter(R.id.ingredients, intent);
    }

    @Override
    public void onEnabled(Context context) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            updateRecipeName(context);
            updateViews(context);
        });
    }

    private void updateRecipeName(Context context) {
        Repository repository = InjectorUtils.getRepository(context);
        Recipe recipe = repository.getLastViewedRecipe();
        recipeName = recipe.getName();
    }

    private void updateViews(Context context) {
        ComponentName provider = new ComponentName(context, IngredientsWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
    }
}

