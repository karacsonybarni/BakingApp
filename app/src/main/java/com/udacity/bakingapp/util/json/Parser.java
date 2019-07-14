package com.udacity.bakingapp.util.json;

import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.json.object.IngredientDescriptor;
import com.udacity.bakingapp.util.json.object.RecipeDescriptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Parser INSTANCE;

    private Parser() {
    }

    private static Parser getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Parser();
        }
        return INSTANCE;
    }

    public static List<Recipe> parseRecipes(String jsonString) throws JSONException {
        JSONArray recipesJSONArray = new JSONArray(jsonString);
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipesJSONArray.length(); i++) {
            JSONObject recipeJSONObject = recipesJSONArray.getJSONObject(i);
            recipes.add(getINSTANCE().parseRecipe(recipeJSONObject));
        }
        return recipes;
    }

    private Recipe parseRecipe(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();
        setId(recipe, jsonObject);
        setName(recipe, jsonObject);
        setIngredients(recipe, jsonObject);
        return recipe;
    }

    private void setId(Recipe recipe, JSONObject jsonObject) throws JSONException {
        long id = jsonObject.getLong(RecipeDescriptor.ID);
        recipe.setId(id);
    }

    private void setName(Recipe recipe, JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString(RecipeDescriptor.NAME);
        recipe.setName(name);
    }

    private void setIngredients(Recipe recipe, JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(RecipeDescriptor.INGREDIENTS);
        List<Ingredient> ingredients = parseIngredients(jsonArray);
        recipe.setIngredients(ingredients);
    }

    public static List<Ingredient> parseIngredients(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        return getINSTANCE().parseIngredients(jsonArray);
    }

    private List<Ingredient> parseIngredients(JSONArray jsonArray) throws JSONException {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ingredients.add(parseIngredient(jsonArray.getJSONObject(i)));
        }
        return ingredients;
    }

    private Ingredient parseIngredient(JSONObject jsonObject) throws JSONException {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(jsonObject.getString(IngredientDescriptor.NAME));
        ingredient.setQuantity(jsonObject.getDouble(IngredientDescriptor.QUANTITY));
        ingredient.setMeasure(jsonObject.getString(IngredientDescriptor.MEASURE));
        return ingredient;
    }
}
