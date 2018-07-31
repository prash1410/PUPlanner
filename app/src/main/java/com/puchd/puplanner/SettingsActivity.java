package com.puchd.puplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        int themeValue = ThemeSetter.getThemeID();
        if(themeValue == 1)setTheme(R.style.AppTheme_Dark_Actionbar);
        getTheme().applyStyle(AccentSetter.getStyleID(),true);
        getTheme().applyStyle(PrimaryColorSetter.getStyleID(),true);

        TypedValue colorAccent = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, colorAccent, true);
        TypedValue uiThemeIconTypedValue  = new TypedValue();
        getTheme().resolveAttribute(R.attr.uiThemeIcon, uiThemeIconTypedValue, true);
        int drawableResourceID = uiThemeIconTypedValue.resourceId;
        Drawable uiThemeIcon = getResources().getDrawable(drawableResourceID);

        ColorFilter filter = new LightingColorFilter(colorAccent.data, colorAccent.data);
        uiThemeIcon.setColorFilter(filter);

        TypedValue uiAccentIconTypedValue  = new TypedValue();
        getTheme().resolveAttribute(R.attr.uiAccentIcon, uiAccentIconTypedValue, true);
        int drawableAccentResourceID = uiAccentIconTypedValue.resourceId;
        Drawable uiAccentIcon = getResources().getDrawable(drawableAccentResourceID);
        uiAccentIcon.setColorFilter(filter);

        TypedValue uiPrimaryColorIconTypedValue  = new TypedValue();
        getTheme().resolveAttribute(R.attr.uiPrimaryColorIcon, uiPrimaryColorIconTypedValue, true);
        int drawablePrimaryColorResourceID = uiPrimaryColorIconTypedValue.resourceId;
        Drawable uiPrimaryColorIcon = getResources().getDrawable(drawablePrimaryColorResourceID);
        uiPrimaryColorIcon.setColorFilter(filter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
