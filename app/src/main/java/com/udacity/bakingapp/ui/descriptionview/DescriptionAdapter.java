package com.udacity.bakingapp.ui.descriptionview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Recipe;

import java.text.DecimalFormat;

class DescriptionAdapter
        extends RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder> {

    private static final int VIEW_TYPE_INGREDIENTS = 0;
    private static final int VIEW_TYPE_STEP = 1;


    private Context context;
    private Recipe recipe;

    DescriptionAdapter(Context context) {
        this.context = context;
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
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_INGREDIENTS) {
            fillIngredientsLayout(holder);
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

    @Override
    public int getItemCount() {
        return recipe != null ? 1 : 0;
    }

    class DescriptionViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup ingredientsLayout;

        DescriptionViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_INGREDIENTS) {
                ingredientsLayout = itemView.findViewById(R.id.ingredients);
            }
        }
    }
}
