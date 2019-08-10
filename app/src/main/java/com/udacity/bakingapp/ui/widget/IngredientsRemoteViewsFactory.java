package com.udacity.bakingapp.ui.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.Nullable;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.InjectorUtils;

import java.util.Objects;

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Repository repository;
    @Nullable private Recipe recipe;

    IngredientsRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        repository = InjectorUtils.getRepository(context);
    }

    @Override
    public void onDataSetChanged() {
        recipe = repository.getLastViewedRecipe();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return recipe != null ? recipe.getIngredients().size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        String ingredient =
                Objects.requireNonNull(recipe).getIngredients().get(i).toString(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient);
        views.setTextViewText(R.id.ingredient, ingredient);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
