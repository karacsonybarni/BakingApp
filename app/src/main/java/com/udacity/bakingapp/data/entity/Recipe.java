package com.udacity.bakingapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey
    private long id;
    private String name;

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
}
