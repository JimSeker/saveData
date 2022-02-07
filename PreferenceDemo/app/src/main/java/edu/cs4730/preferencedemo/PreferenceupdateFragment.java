package edu.cs4730.preferencedemo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.util.Log;
import java.util.Set;

/**
 * this version, not only shows the preference screen, but update the summary fields with the answer the user picked.
 *   See the xml for preference entries.
 */

public class PreferenceupdateFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private EditTextPreference mEditTextPreference;
    private ListPreference mListPreference;
    private MultiSelectListPreference mMultiSelectListPreference;
    private String TAG = "PreferenceupdateFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefupdate);

        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mEditTextPreference = (EditTextPreference) getPreferenceScreen().findPreference("textPref");
        mListPreference = (ListPreference) getPreferenceScreen().findPreference("list_preference");
        mMultiSelectListPreference = (MultiSelectListPreference) getPreferenceScreen().findPreference("multiselect_key");
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup the initial values, with the defaults.
        mEditTextPreference.setSummary("Text is " + mEditTextPreference.getSharedPreferences().getString("textPref", "Default"));
        mListPreference.setSummary("Current value is " + mListPreference.getSharedPreferences().getString("list_preference", "Default"));
        // multiselect returns a array set, not a string, so create one.
        String list = "";
        Set<String> selections = mMultiSelectListPreference.getSharedPreferences().getStringSet("multiselect_key", null);
        for (String s : selections) {
            Log.wtf(TAG, "value is " + s);
            list += s + " ";
        }
        mMultiSelectListPreference.setSummary("selection  is " + list);

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
        //as the preferences are updated, change the summary section.
        if (key.equals("textPref")) {  //where textPref is the key used in the xml.
            mEditTextPreference.setSummary("Text is " + sharedPreferences.getString("textPref", "Default"));
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
        }
    }


}
