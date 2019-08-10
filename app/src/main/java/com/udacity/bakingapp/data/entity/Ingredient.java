package com.udacity.bakingapp.data.entity;

import android.content.Context;

import com.udacity.bakingapp.R;

import java.text.DecimalFormat;

public class Ingredient {

    private String name;
    private double quantity;
    private String measure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String toString(Context context) {
        String quantity = new DecimalFormat("#.##").format(getQuantity());
        return context.getString(
                R.string.ingredient,
                quantity, getMeasure(), getName());
    }
}
