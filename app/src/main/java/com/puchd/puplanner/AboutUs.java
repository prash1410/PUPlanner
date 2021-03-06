package com.puchd.puplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

}
