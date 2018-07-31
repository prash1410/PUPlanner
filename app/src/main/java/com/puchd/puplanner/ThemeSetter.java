package com.puchd.puplanner;

import android.content.Context;
import android.preference.PreferenceManager;

public class ThemeSetter
{
    private static int themeID;

    public static int getThemeID()
    {
        return themeID;
    }

    public static void setThemeID(Context context)
    {
        themeID = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("themeString", "0"));
    }
}
