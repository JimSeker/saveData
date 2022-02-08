package edu.cs4730.supportprefencedemo;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

/**
 * A holder for the advanced preferences xml.
 */
public class AdvancedPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferencesadvanced, rootKey);
    }
}
