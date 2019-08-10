package com.udacity.bakingapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Recipe {

    @PrimaryKey
    private long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private long lastViewed;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public long getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(long lastViewed) {
        this.lastViewed = lastViewed;
    }
}
