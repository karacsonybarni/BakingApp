package com.udacity.bakingapp.util.json;

import com.udacity.bakingapp.data.entity.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final String ID = "id";
    private static final String NAME = "name";

    public static List<Recipe> parseRecipes(String jsonString) throws JSONException {
        JSONArray recipesJSONArray = new JSONArray(jsonString);
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < recipesJSONArray.length(); i++) {
            JSONObject recipeJSONObject = recipesJSONArray.getJSONObject(i);
            recipes.add(parseRecipe(recipeJSONObject));
        }
        return recipes;
    }

    private static Recipe parseRecipe(JSONObject jsonObject) throws JSONException {
        Recipe recipe = new Recipe();
        recipe.setId(jsonObject.getLong(ID));
        recipe.setName(jsonObject.getString(NAME));
        return recipe;
    }
}
