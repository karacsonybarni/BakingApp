package com.udacity.bakingapp.data.network;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.data.entity.Recipe;
import com.udacity.bakingapp.util.json.Parser;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RecipesNetworkDataSource {

    private static RecipesNetworkDataSource sInstance;
    private MutableLiveData<List<Recipe>> recipesLiveData;
    private RecipeService recipeService;

    private RecipesNetworkDataSource(Context context) {
        recipesLiveData = new MutableLiveData<>();
        initRetrofit(context);
    }

    private void initRetrofit(Context context) {
        String baseUrl = context.getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        recipeService = retrofit.create(RecipeService.class);
    }

    public static RecipesNetworkDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RecipesNetworkDataSource(context);
        }
        return sInstance;
    }

    public void fetchRecipes() {
        //noinspection NullableProblems
        recipeService.getRecipesJson().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    List<Recipe> recipes = Parser.parseRecipes(response.body());
                    setRecipes(recipes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipesLiveData;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipesLiveData.postValue(recipes);
    }
}
