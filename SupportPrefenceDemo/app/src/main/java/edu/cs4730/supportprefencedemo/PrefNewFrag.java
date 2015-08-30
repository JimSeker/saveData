package edu.cs4730.supportprefencedemo;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.preference.PreferenceFragmentCompat;
//import android.support.v14.preference.PreferenceFragment;

/**
 * Using  v7, instead of v14, because it caused weird compile errors with the support fragments in mainActivity.
 *
 *  There is commented out code, hoping the MultiSelectList preference will be back ported to v7 from v14.
 *
 */
public class PrefNewFrag extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.newperferences);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //switchpreference really doesn't have summary.  this is left for the multiselectlist.
    }

}
