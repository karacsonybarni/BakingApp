package com.udacity.bakingapp.ui.descriptionview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.ui.stepview.StepActivity;

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
        switch (viewType) {
            case VIEW_TYPE_INGREDIENTS:
                return R.layout.ingredients;
            case VIEW_TYPE_STEP:
                return R.layout.short_description;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_INGREDIENTS:
                fillIngredientsLayoutIfEmpty(holder);
                return;
            case VIEW_TYPE_STEP:
                fillStepLayout(holder, position);
        }
    }

    private void fillIngredientsLayoutIfEmpty(DescriptionViewHolder holder) {
        ViewGroup ingredientsLayout = holder.ingredientsLayout;
        if (isIngredientsLayoutEmpty(ingredientsLayout)) {
            fillIngredientsLayout(ingredientsLayout);
        }
    }

    private boolean isIngredientsLayoutEmpty(ViewGroup ingredientsLayout) {
        return ingredientsLayout.getChildCount() == 1;
    }

    private void fillIngredientsLayout(ViewGroup ingredientsLayout) {
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
        int stepPosition = position - 1;
        Step step = recipe.getSteps().get(stepPosition);
        holder.shortDescription.setText(step.getShortDescription());
        holder.itemView.setOnClickListener(getOnClickListener(stepPosition));
    }

    private View.OnClickListener getOnClickListener(int position) {
        return view -> {
            Intent intent = new Intent(context, StepActivity.class);
            intent.putExtra(StepActivity.RECIPE_ID_EXTRA, recipe.getId());
            intent.putExtra(StepActivity.STEP_POSITION_EXTRA, position);
            context.startActivity(intent);
        };
    }

    @Override
    public int getItemCount() {
        return recipe != null ? recipe.getSteps().size() + 1 : 0;
    }

    class DescriptionViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup ingredientsLayout;

        private TextView shortDescription;

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
            shortDescription = (TextView) itemView;
        }
    }
}
