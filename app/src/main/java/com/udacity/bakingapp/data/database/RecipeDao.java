package com.udacity.bakingapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.udacity.bakingapp.data.entity.Recipe;

import java.util.List;

@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM recipe WHERE id = :id")
    public abstract LiveData<Recipe> getRecipe(long id);

    @Query("SELECT * FROM recipe")
    public abstract LiveData<List<Recipe>> getRecipes();

    @Transaction
    public void updateRecipes(List<Recipe> recipes) {
        deleteAllRecipes();
        bulkInsert(recipes);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void bulkInsert(List<Recipe> recipes);

    @Query("DELETE FROM recipe")
    abstract void deleteAllRecipes();
}
