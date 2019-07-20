package com.udacity.bakingapp.ui.recipesview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.ui.descriptionview.DescriptionActivity;

import java.util.List;

class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    RecipesAdapter(Context context) {
        this.context = context;
    }

    void updateAll(List<Recipe> recipes) {
        this.recipes = recipes;
        if (recipes != null) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View posterView =
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.card_recipe, parent, false);
        return new RecipeViewHolder(posterView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.getName());
        setOnClickListener(holder, recipe);
    }

    private void setOnClickListener(RecipeViewHolder holder, Recipe recipe) {
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DescriptionActivity.class);
            intent.putExtra(DescriptionActivity.RECIPE_ID_EXTRA, recipe.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
