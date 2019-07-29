package com.udacity.bakingapp.util;

import android.content.Context;
import android.content.res.Configuration;

public class ConfigurationUtils {

    public static boolean isTablet(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.smallestScreenWidthDp >= 600;
    }

    public static boolean isInLandscapeMode(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
