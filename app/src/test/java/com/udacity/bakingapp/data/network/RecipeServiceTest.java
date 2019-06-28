package com.udacity.bakingapp.data.network;

import com.udacity.bakingapp.data.database.entity.Recipe;
import com.udacity.bakingapp.util.json.Parser;

import org.assertj.core.api.Fail;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeServiceTest {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    private static final int NUTELLA_PIE_INDEX = 0;
    private static final String NUTELLA_PIE_NAME = "Nutella Pie";

    private final CountDownLatch latch = new CountDownLatch(1);
    private RecipeService recipeService;
    private Recipe nutellaPie;

    @Before
    public void setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        recipeService = retrofit.create(RecipeService.class);
    }

    @Test
    public void testGetRecipesJson() throws InterruptedException {
        //noinspection NullableProblems
        recipeService.getRecipesJson().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String json = response.body();
                try {
                    List<Recipe> recipes = Parser.parseRecipes(json);
                    Recipe nutellaPieFromSource = recipes.get(NUTELLA_PIE_INDEX);
                    assertThat(nutellaPieFromSource.getName()).isEqualTo(getNutellaPie().getName());
                    latch.countDown();
                } catch (JSONException e) {
                    Fail.fail("JSON could not be parsed", e);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Fail.fail("Failed to get recipes json from source", t);
                latch.countDown();
            }
        });
        latch.await();
    }

    private Recipe getNutellaPie() {
        if (nutellaPie ==  null) {
            nutellaPie = new Recipe();
            nutellaPie.setName(NUTELLA_PIE_NAME);
        }
        return nutellaPie;
    }
}