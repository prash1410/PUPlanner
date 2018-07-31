package com.puchd.puplanner;

import android.content.Context;
import android.preference.PreferenceManager;

public class PrimaryColorSetter
{
    private static int styleID;

    public static int getStyleID()
    {
        return styleID;
    }

    public static void setStyleID(Context context)
    {
        styleID = PreferenceManager.getDefaultSharedPreferences(context).getInt("colorPrimary", R.style.PrimaryBlue);
    }
}
