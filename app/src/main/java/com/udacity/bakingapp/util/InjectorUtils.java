package com.udacity.bakingapp.util;

import android.content.Context;

import com.udacity.bakingapp.data.Repository;
import com.udacity.bakingapp.data.database.Database;
import com.udacity.bakingapp.data.network.RecipesNetworkDataSource;

public class InjectorUtils {

    public static Repository getRepository(Context context) {
        Database database = Database.getInstance(context);
        RecipesNetworkDataSource networkDataSource = RecipesNetworkDataSource.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        return Repository.getInstance(database, networkDataSource, executors);
    }
}
