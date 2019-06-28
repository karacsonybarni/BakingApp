package com.udacity.bakingapp.data.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<String> getRecipesJson();
}
