package com.udacity.bakingapp.ui.steplistview;

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
import com.udacity.bakingapp.data.entity.Step;

import java.text.DecimalFormat;

class StepListAdapter
        extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private static final int VIEW_TYPE_INGREDIENTS = 0;
    private static final int VIEW_TYPE_STEP = 1;

    private Context context;
    private Recipe recipe;
    private OnSelectionListener selectionListener;
    private int stepPosition;

    StepListAdapter(Context context, OnSelectionListener selectionListener) {
        this.context = context;
        this.selectionListener = selectionListener;
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
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = getLayoutIdByViewType(viewType);
        View layout = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new StepViewHolder(layout, viewType);
    }

    private int getLayoutIdByViewType(int viewType) {
        switch (viewType) {
            case VIEW_TYPE_INGREDIENTS:
                return R.layout.ingredients;
            case VIEW_TYPE_STEP:
                return R.layout.list_item_step;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_INGREDIENTS:
                fillIngredientsLayoutIfEmpty(holder);
                return;
            case VIEW_TYPE_STEP:
                fillStepLayout(holder, position);
        }
    }

    private void fillIngredientsLayoutIfEmpty(StepViewHolder holder) {
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

    private void fillStepLayout(StepViewHolder holder, int position) {
        int stepPosition = position - 1;
        Step step = recipe.getSteps().get(stepPosition);
        holder.itemView.setSelected(this.stepPosition == stepPosition);
        holder.shortDescription.setText(step.getShortDescription());
        holder.itemView.setOnClickListener(getOnClickListener(stepPosition));
    }

    private View.OnClickListener getOnClickListener(int stepPosition) {
        return view -> selectionListener.onSelect(stepPosition);
    }

    void updateStepPosition(int stepPosition) {
        if (this.stepPosition == stepPosition) {
            return;
        }
        int oldStepPosition = this.stepPosition;
        this.stepPosition = stepPosition;
        if (hasSteps()) {
            notifyStepItemChanged(oldStepPosition);
            notifyStepItemChanged(stepPosition);
        }
    }

    private boolean hasSteps() {
        return getItemCount() > 1;
    }

    private void notifyStepItemChanged(int stepPosition) {
        int positionInAdapter = stepPosition + 1;
        notifyItemChanged(positionInAdapter);
    }

    @Override
    public int getItemCount() {
        return recipe != null ? recipe.getSteps().size() + 1 : 0;
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup ingredientsLayout;

        private TextView shortDescription;

        StepViewHolder(@NonNull View itemView, int viewType) {
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
