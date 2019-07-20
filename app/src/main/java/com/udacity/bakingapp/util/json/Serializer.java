package com.udacity.bakingapp.util.json;

import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.util.json.descriptor.IngredientDescriptor;
import com.udacity.bakingapp.util.json.descriptor.StepDescriptor;

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
        JSONArray jsonArray = new JSONArray();
        for (Ingredient ingredient : ingredients) {
            jsonArray.put(getInstance().serializeIngredient(ingredient));
        }
        return jsonArray.toString();
    }

    private JSONObject serializeIngredient(Ingredient ingredient) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(IngredientDescriptor.NAME, ingredient.getName());
        jsonObject.put(IngredientDescriptor.QUANTITY, ingredient.getQuantity());
        jsonObject.put(IngredientDescriptor.MEASURE, ingredient.getMeasure());
        return jsonObject;
    }

    public static String serializeSteps(List<Step> steps) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Step step : steps) {
            jsonArray.put(getInstance().serializeStep(step));
        }
        return jsonArray.toString();
    }

    private JSONObject serializeStep(Step step) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StepDescriptor.ID, step.getId());
        jsonObject.put(StepDescriptor.SHORT_DESCRIPTION, step.getShortDescription());
        jsonObject.put(StepDescriptor.DESCRIPTION, step.getDescription());
        jsonObject.put(StepDescriptor.VIDEO_URL, step.getVideoURL());
        jsonObject.put(StepDescriptor.THUMBNAIL_URL, step.getThumbnailURL());
        return jsonObject;
    }
}
