package com.udacity.bakingapp.data.database;

import androidx.room.TypeConverter;

import com.udacity.bakingapp.data.entity.Ingredient;
import com.udacity.bakingapp.util.json.Parser;
import com.udacity.bakingapp.util.json.Serializer;

import org.json.JSONException;

import java.util.List;

@SuppressWarnings("WeakerAccess")
class IngredientConverter {

    @TypeConverter
    public static List<Ingredient> toIngredients(String string) {
        try {
            return Parser.parseIngredients(string);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toString(List<Ingredient> ingredients) {
        try {
            return Serializer.serializeIngredients(ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
