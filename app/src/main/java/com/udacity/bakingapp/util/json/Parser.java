package com.udacity.bakingapp.util.json;

import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.util.json.descriptor.IngredientDescriptor;
import com.udacity.bakingapp.util.json.descriptor.RecipeDescriptor;
import com.udacity.bakingapp.util.json.descriptor.StepDescriptor;

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
        setSteps(recipe, jsonObject);
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

    private void setSteps(Recipe recipe, JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(RecipeDescriptor.STEPS);
        List<Step> steps = parseSteps(jsonArray);
        recipe.setSteps(steps);
    }

    public static List<Step> parseSteps(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        return getINSTANCE().parseSteps(jsonArray);
    }

    private List<Step> parseSteps(JSONArray jsonArray) throws JSONException {
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            steps.add(parseStep(jsonArray.getJSONObject(i)));
        }
        return steps;
    }

    private Step parseStep(JSONObject jsonObject) throws JSONException {
        Step step = new Step();
        step.setId(jsonObject.getLong(StepDescriptor.ID));
        step.setShortDescription(jsonObject.getString(StepDescriptor.SHORT_DESCRIPTION));
        step.setDescription(jsonObject.getString(StepDescriptor.DESCRIPTION));
        step.setVideoURL(jsonObject.getString(StepDescriptor.VIDEO_URL));
        step.setThumbnailURL(jsonObject.getString(StepDescriptor.THUMBNAIL_URL));
        return step;
    }
}
