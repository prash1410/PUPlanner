package com.puchd.puplanner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_screen);

        Preference accentPreference = getPreferenceScreen().findPreference("accentPicker");
        accentPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                createAccentPickerDialog();
                return true;
            }
        });

        Preference primaryColorPreference = getPreferenceScreen().findPreference("primaryColor");
        primaryColorPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                createPrimaryColorPickerDialog();
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(key.equals("themeString"))
        {
            ThemeSetter.setThemeID(getActivity());
            getActivity().finish();
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    public void createAccentPickerDialog()
    {
        AlertDialog.Builder Confirmation = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_accent_picker,null);
        TextView red,pink,purple,deepPurple,indigo,blue,cyan,teal,green,lightGreen,lime,yellow,orange,deepOrange,amber;

        red = dialogView.findViewById(R.id.red);
        pink = dialogView.findViewById(R.id.pink);
        purple = dialogView.findViewById(R.id.purple);
        deepPurple = dialogView.findViewById(R.id.deepPurple);
        indigo = dialogView.findViewById(R.id.indigo);
        blue = dialogView.findViewById(R.id.blue);
        cyan = dialogView.findViewById(R.id.cyan);
        teal = dialogView.findViewById(R.id.teal);
        green = dialogView.findViewById(R.id.green);
        lightGreen = dialogView.findViewById(R.id.lightGreen);
        lime = dialogView.findViewById(R.id.lime);
        yellow = dialogView.findViewById(R.id.yellow);
        orange = dialogView.findViewById(R.id.orange);
        deepOrange = dialogView.findViewById(R.id.deepOrange);
        amber = dialogView.findViewById(R.id.amber);

        red.setOnClickListener(this);
        pink.setOnClickListener(this);
        purple.setOnClickListener(this);
        deepPurple.setOnClickListener(this);
        indigo.setOnClickListener(this);
        blue.setOnClickListener(this);
        cyan.setOnClickListener(this);
        teal.setOnClickListener(this);
        green.setOnClickListener(this);
        lightGreen.setOnClickListener(this);
        lime.setOnClickListener(this);
        yellow.setOnClickListener(this);
        orange.setOnClickListener(this);
        deepOrange.setOnClickListener(this);
        amber.setOnClickListener(this);

        Confirmation.setView(dialogView)
                .setTitle("Pick an accent")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });
        AlertDialog Conf = Confirmation.create();
        Conf.show();
    }

    public void createPrimaryColorPickerDialog()
    {
        AlertDialog.Builder Confirmation = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_primary_color_picker,null);
        TextView red,pink,purple,deepPurple,indigo,blue,cyan,teal,green,lightGreen,lime,yellow,orange,deepOrange,amber,brown,grey,blueGrey,darkGrey,black;

        red = dialogView.findViewById(R.id.redPrimary);
        pink = dialogView.findViewById(R.id.pinkPrimary);
        purple = dialogView.findViewById(R.id.purplePrimary);
        deepPurple = dialogView.findViewById(R.id.deepPurplePrimary);
        indigo = dialogView.findViewById(R.id.indigoPrimary);
        blue = dialogView.findViewById(R.id.bluePrimary);
        cyan = dialogView.findViewById(R.id.cyanPrimary);
        teal = dialogView.findViewById(R.id.tealPrimary);
        green = dialogView.findViewById(R.id.greenPrimary);
        lightGreen = dialogView.findViewById(R.id.lightGreenPrimary);
        lime = dialogView.findViewById(R.id.limePrimary);
        yellow = dialogView.findViewById(R.id.yellowPrimary);
        orange = dialogView.findViewById(R.id.orangePrimary);
        deepOrange = dialogView.findViewById(R.id.deepOrangePrimary);
        amber = dialogView.findViewById(R.id.amberPrimary);
        brown = dialogView.findViewById(R.id.brownPrimary);
        grey = dialogView.findViewById(R.id.greyPrimary);
        blueGrey = dialogView.findViewById(R.id.blueGreyPrimary);
        darkGrey = dialogView.findViewById(R.id.darkGreyPrimary);
        black = dialogView.findViewById(R.id.blackPrimary);

        red.setOnClickListener(this);
        pink.setOnClickListener(this);
        purple.setOnClickListener(this);
        deepPurple.setOnClickListener(this);
        indigo.setOnClickListener(this);
        blue.setOnClickListener(this);
        cyan.setOnClickListener(this);
        teal.setOnClickListener(this);
        green.setOnClickListener(this);
        lightGreen.setOnClickListener(this);
        lime.setOnClickListener(this);
        yellow.setOnClickListener(this);
        orange.setOnClickListener(this);
        deepOrange.setOnClickListener(this);
        amber.setOnClickListener(this);
        brown.setOnClickListener(this);
        grey.setOnClickListener(this);
        blueGrey.setOnClickListener(this);
        darkGrey.setOnClickListener(this);
        black.setOnClickListener(this);

        Confirmation.setView(dialogView)
                .setTitle("Pick a primary color")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });
        AlertDialog Conf = Confirmation.create();
        Conf.show();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.red:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.RedAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.pink:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.PinkAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.purple:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.PurpleAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.deepPurple:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.DeepPurpleAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.indigo:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.IndigoAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.blue:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.BlueAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.cyan:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.CyanAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.teal:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.TealAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.green:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.GreenAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.lightGreen:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.LightGreenAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.lime:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.LimeAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.yellow:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.YellowAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.orange:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.OrangeAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.deepOrange:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.DeepOrangeAccent).apply();
                setAccentReloadActivity();
                break;
            }
            case R.id.amber:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("accentPicker",R.style.AmberAccent).apply();
                setAccentReloadActivity();
                break;
            }





            case R.id.redPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryRed).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.pinkPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryPink).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.purplePrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryPurple).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.deepPurplePrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryDeepPurple).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.indigoPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryIndigo).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.bluePrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryBlue).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.cyanPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryCyan).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.tealPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryTeal).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.greenPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryGreen).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.lightGreenPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryLightGreen).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.limePrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryLime).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.yellowPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryYellow).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.orangePrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryOrange).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.deepOrangePrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryDeepOrange).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.amberPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryAmber).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.brownPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryBrown).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.greyPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryGrey).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.blueGreyPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryBlueGrey).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.darkGreyPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryDarkGrey).apply();
                setPrimaryColorReloadActivity();
                break;
            }
            case R.id.blackPrimary:
            {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().putInt("colorPrimary",R.style.PrimaryBlack).apply();
                setPrimaryColorReloadActivity();
                break;
            }
        }
    }

    public void setAccentReloadActivity()
    {
        AccentSetter.setStyleID(getActivity());
        getActivity().finish();
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    public void setPrimaryColorReloadActivity()
    {
        PrimaryColorSetter.setStyleID(getActivity());
        getActivity().finish();
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }
}