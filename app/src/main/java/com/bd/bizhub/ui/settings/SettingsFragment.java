package com.bd.bizhub.ui.settings;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bd.bizhub.OnboardingActivity;
import com.bd.bizhub.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);




        SwitchPreferenceCompat themeSwitch =  findPreference("theme");
        themeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            Boolean isdarkOn = (Boolean) newValue;
            toggleTheme(isdarkOn);
            return true;
        });
    }

    public void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        Log.d("Theme", String.valueOf(darkTheme));
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();


        Intent intent = new Intent(getContext(), OnboardingActivity.class);
        //intent.putExtra("from_setting","yes");
        startActivity(intent);
        getActivity().finishAffinity();
    }
}
