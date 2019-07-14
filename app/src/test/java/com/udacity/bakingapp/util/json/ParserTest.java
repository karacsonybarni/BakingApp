package com.udacity.bakingapp.util.json;

import com.udacity.bakingapp.TestData;
import com.udacity.bakingapp.data.entity.Recipe;

import org.json.JSONException;
import org.junit.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {

    @Test
    public void parseRecipes() throws JSONException {
        List<Recipe> recipes = Parser.parseRecipes(TestData.RECIPES_JSON);
        Recipe nutellaPie = recipes.get(0);
        assertThat(nutellaPie.getName()).isEqualTo("Nutella Pie");
        assertThat(nutellaPie.getIngredients().get(0).getName()).isEqualTo("Graham Cracker crumbs");
    }
}