package com.udacity.bakingapp.util.json;

import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.util.json.object.IngredientDescriptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Serializer {

    private static Serializer INSTANCE;

    private Serializer() {
    }

    private static Serializer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Serializer();
        }
        return INSTANCE;
    }

    public static String serializeIngredients(List<Ingredient> ingredients) throws JSONException {
        JSONArray array = new JSONArray();
        for (Ingredient ingredient : ingredients) {
            array.put(getInstance().serializeIngredient(ingredient));
        }
        return array.toString();
    }

    private JSONObject serializeIngredient(Ingredient ingredient) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IngredientDescriptor.NAME, ingredient.getName());
        jsonObject.put(IngredientDescriptor.QUANTITY, ingredient.getQuantity());
        jsonObject.put(IngredientDescriptor.MEASURE, ingredient.getMeasure());
        return jsonObject;
    }
}
