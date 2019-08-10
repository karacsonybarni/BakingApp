package com.udacity.bakingapp.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.udacity.bakingapp.data.entity.Recipe;

@androidx.room.Database(entities = {Recipe.class}, version = 3, exportSchema = false)
@androidx.room.TypeConverters(TypeConverters.class)
public abstract class Database extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipes";
    private static Database sInstance;

    public static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = createInstance(context);
            }
        }
        return sInstance;
    }

    @Override
    public void close() {
        super.close();
        sInstance = null;
    }

    private static Database createInstance(Context context) {
        return getDatabaseBuilder(context).build();
    }

    private static RoomDatabase.Builder<Database> getDatabaseBuilder(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), Database.class, DATABASE_NAME)
                .fallbackToDestructiveMigration();
    }

    public abstract RecipeDao recipeDao();
}
