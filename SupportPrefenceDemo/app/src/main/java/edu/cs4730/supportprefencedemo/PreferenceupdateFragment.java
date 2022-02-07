package edu.cs4730.supportprefencedemo;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

import androidx.preference.DropDownPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * The support version of the preference fragment.
 */
public class PreferenceupdateFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference mEditTextPreference;
    private ListPreference mListPreference;
    private MultiSelectListPreference mMultiSelectListPreference;
    private DropDownPreference mDropDownPreference;
    private String TAG = "PreferenceupdateFragment";

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mEditTextPreference = getPreferenceScreen().findPreference("edittext_preference");
        mListPreference = getPreferenceScreen().findPreference("list_preference");
        mMultiSelectListPreference = getPreferenceManager().findPreference("multiselect_key");
        mDropDownPreference = getPreferenceManager().findPreference("dropdown");
    }


    @Override
    public void onResume() {
        super.onResume();

        // Setup the initial values
        mEditTextPreference.setSummary("Text is " + mEditTextPreference.getSharedPreferences().getString("edittext_preference", "Default"));
        mListPreference.setSummary("Current value is " + mListPreference.getSharedPreferences().getString("list_preference", "Default"));
        // multiselect returns a array set, not a string, so create one.
        String list = "";
        Set<String> selections = mMultiSelectListPreference.getSharedPreferences().getStringSet("multiselect_key", null);
        for (String s : selections) {
            Log.wtf(TAG, "value is " + s);
            list += s + " ";
        }
        mMultiSelectListPreference.setSummary("selection  is " + list);

        mDropDownPreference.setSummary("Current value is " + mDropDownPreference.getSharedPreferences().getString("dropdown", "Default"));

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
        if (key.equals("edittext_preference")) {  //where textPref is the key used in the xml.
            mEditTextPreference.setSummary("Text is " + sharedPreferences.getString("edittext_preference", "Default"));
        } else if (key.equals("list_preference")) {
            mListPreference.setSummary("Current value is " + sharedPreferences.getString(key, "Default"));
        } else if (key.equals("multiselect_key")) {
            String list = "";
            Set<String> selections = mMultiSelectListPreference.getSharedPreferences().getStringSet("multiselect_key", null);
            for (String s : selections) {
                //Log.wtf(TAG, "value is " + s);
                list += s + " ";
            }
            mMultiSelectListPreference.setSummary("selection  is " + list);
        } else if (key.equals("dropdown")) {
            mDropDownPreference.setSummary("Current value is " + mDropDownPreference.getSharedPreferences().getString("dropdown", "Default"));
        }
    }
}
