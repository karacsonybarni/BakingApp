package com.udacity.bakingapp.ui.descriptionview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.InjectorUtils;

import java.text.DecimalFormat;
import java.util.Objects;

public class DescriptionActivity extends AppCompatActivity {

    public static final String RECIPE_ID_EXTRA = "recipeId";

    private DescriptionActivityViewModel viewModel;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        viewModel = getViewModel(getRecipeId());
        populateViews();
    }

    private long getRecipeId() {
        return Objects.requireNonNull(getIntent().getExtras()).getLong(RECIPE_ID_EXTRA);
    }

    private DescriptionActivityViewModel getViewModel(long id) {
        Repository repository = InjectorUtils.getRepository(this);
        DescriptionViewModelFactory factory = new DescriptionViewModelFactory(repository, id);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(DescriptionActivityViewModel.class);
    }

    private void populateViews() {
        viewModel.getRecipe().observe(this, this::populateViews);
    }

    private void populateViews(Recipe recipe) {
        this.recipe = recipe;
        setTitle();
        fillIngredientsLayout();
    }

    private void setTitle() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipe.getName());
    }

    private void fillIngredientsLayout() {
        ViewGroup ingredientsLayout = findViewById(R.id.ingredients);
        for (Ingredient ingredient : recipe.getIngredients()) {
            TextView ingredientView =
                    (TextView) LayoutInflater
                            .from(this)
                            .inflate(R.layout.ingredient, ingredientsLayout, false);
            ingredientView.setText(getIngredientText(ingredient));
            ingredientsLayout.addView(ingredientView);
        }
    }

    private String getIngredientText(Ingredient ingredient) {
        String quantity = new DecimalFormat("#.##").format(ingredient.getQuantity());
        return getString(
                R.string.ingredient,
                quantity, ingredient.getMeasure(), ingredient.getName());
    }
}
