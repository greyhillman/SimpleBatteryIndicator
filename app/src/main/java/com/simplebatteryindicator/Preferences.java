package com.simplebatteryindicator;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *  This is a class that handles all the settings the Indicator has.
 *
 *  Right now it uses SharedPreferences to handle the settings. This is an Android class that handles
 *  preferences for an activity. It stores variables given a string key and we access those variables
 *  with those keys. These keys are the variable's name. It stores these variables in a file, which
 *  we called FILE_NAME. We can have multiple preference files but for this application we only
 *  need one.
 */
public final class Preferences {
    private static final String FILE_NAME = "batteryPreference";

    public static final String  PERIOD         = "batteryPreference_period";
    public static final int     PERIOD_DEFAULT = 60;
    public static final String  SAVING         = "batteryPreference_saving";
    public static final boolean SAVING_DEFAULT = true;

    private static SharedPreferences settings;

    // Used to setup the preferences once
    public static void setUpPreferences(Context context) {
        if (settings == null) {
            settings = context.getSharedPreferences(FILE_NAME, 0);
        }
    }

    public static int getInt(String variable, int defaultValue) {
        return settings.getInt(variable, defaultValue);
    }

    public static boolean getBoolean(String variable, boolean defaultValue) {
        return settings.getBoolean(variable, defaultValue);
    }

    public static void setInt(String variable, int value) {
        settings.edit().putInt(variable, value).apply();
    }

    public static void setBoolean(String variable, boolean value) {
        settings.edit().putBoolean(variable, value).apply();
    }
}
