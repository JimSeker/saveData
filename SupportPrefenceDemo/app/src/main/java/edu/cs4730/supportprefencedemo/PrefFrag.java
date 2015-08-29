package edu.cs4730.supportprefencedemo;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * A simple support preference fragment.   see the xml for most of the data.
 */
public class PrefFrag extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
