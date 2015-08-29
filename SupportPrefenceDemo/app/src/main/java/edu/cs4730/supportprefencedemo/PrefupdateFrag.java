package edu.cs4730.supportprefencedemo;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;


/**
 * The support version of the preference fragment.
 */
public class PrefupdateFrag extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference mEditTextPreference;
    private ListPreference mListPreference;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.prefupdate);

        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mEditTextPreference = (EditTextPreference)getPreferenceScreen().findPreference("textPref");
        mListPreference = (ListPreference)getPreferenceScreen().findPreference("list_preference");
    }
    @Override
    public void onResume() {
        super.onResume();

        // Setup the initial values
        mEditTextPreference.setSummary( "Text is " + mEditTextPreference.getSharedPreferences().getString("textPref", "Default"));
        mListPreference.setSummary("Current value is " + mListPreference.getSharedPreferences().getString("list_preference", "Default"));

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("textPref")) {  //where textPref is the key used in the xml.
            mEditTextPreference.setSummary( "Text is " + sharedPreferences.getString("textPref", "Default"));
        }  else if (key.equals("list_preference")) {

            mListPreference.setSummary("Current value is " + sharedPreferences.getString(key, "Default"));
        }
    }
}
