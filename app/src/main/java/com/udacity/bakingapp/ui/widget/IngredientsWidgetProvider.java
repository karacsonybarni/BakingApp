package com.udacity.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static IngredientsWidgetProvider instance;
    private Recipe recipe;

    private static IngredientsWidgetProvider getInstance() {
        if (instance == null) {
            instance = new IngredientsWidgetProvider();
        }
        return instance;
    }

    public static void updateWidgets(
            Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        IngredientsWidgetProvider ingredientsWidgetProvider = getInstance();
        ingredientsWidgetProvider.recipe = recipe;
        ingredientsWidgetProvider.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (recipe == null) {
            return;
        }
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(
            Context context, AppWidgetManager appWidgetManager, int widgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        updateRecipe(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    private void updateRecipe(Context context, RemoteViews views) {
        views.setTextViewText(R.id.recipeName, recipe.getName());
        updateIngredients(context, views);
    }

    private void updateIngredients(Context context, RemoteViews views) {
        Intent intent = new Intent(context, IngredientsRemoteViewsService.class);
        views.setRemoteAdapter(R.id.ingredients, intent);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

