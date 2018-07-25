package com.puchd.puplanner;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference_screen);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /*
        // To get a preference
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference preference = preferenceScreen.findPreference("preference_ key_defined_in_the_xml");

        //You can set a listener
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

        //change title
        preference.setTitle("my_title");

        // etc
        */
    }
}