package com.udacity.bakingapp.data.database;

import androidx.room.TypeConverter;

import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.data.entity.Step;
import com.udacity.bakingapp.util.json.Parser;
import com.udacity.bakingapp.util.json.Serializer;

import org.json.JSONException;

import java.util.List;

@SuppressWarnings("WeakerAccess")
class TypeConverters {

    @TypeConverter
    public static List<Ingredient> toIngredients(String ingredientsString) {
        try {
            return Parser.parseIngredients(ingredientsString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toIngredientsString(List<Ingredient> ingredients) {
        try {
            return Serializer.serializeIngredients(ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static List<Step> toSteps(String string) {
        try {
            return Parser.parseSteps(string);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toStepsString(List<Step> steps) {
        try {
            return Serializer.serializeSteps(steps);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
