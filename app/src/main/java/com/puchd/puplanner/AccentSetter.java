package com.puchd.puplanner;

import android.content.Context;
import android.preference.PreferenceManager;

public class AccentSetter
{
    private static int styleID;

    public static int getStyleID()
    {
        return styleID;
    }

    public static void setStyleID(Context context)
    {
        styleID = PreferenceManager.getDefaultSharedPreferences(context).getInt("accentPicker", R.style.BlueAccent);
    }
}
