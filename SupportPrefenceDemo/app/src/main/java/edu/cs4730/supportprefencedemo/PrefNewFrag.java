package edu.cs4730.supportprefencedemo;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
//import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.MultiSelectListPreference;
import android.util.Log;
import java.util.Set;

/**
 * Using  v7, instead of v14, because it caused support fragment and v14 is not support fragment compile errors with the support fragments in mainActivity.
 *
 *  yet, the v14 works, since we are at API 16.  android is just weird sometimes.
 */
public class PrefNewFrag extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    String TAG = "PrefNewFrag";
    MultiSelectListPreference mMultiSelectListPreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.newperferences, rootKey);
        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mMultiSelectListPreference = (MultiSelectListPreference) getPreferenceScreen().findPreference("multiselect_key");
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup the initial values   multiselect return a array set, not a string
        String list = "";
        Set<String> selections = mMultiSelectListPreference.getSharedPreferences().getStringSet("multiselect_key", null);
        for(String s: selections) {
             Log.wtf(TAG, "value is "+ s);
            list += s + " ";
        }
        mMultiSelectListPreference.setSummary("selection  is " + list );

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Log.wtf(TAG, "key is " + key);
        //switchpreference really doesn't have summary.  this is for the multiselectlist.
        if (key.equals("multiselect_key")) {  //where textPref is the key used in the xml.
            String list = "";
            Set<String> selections = mMultiSelectListPreference.getSharedPreferences().getStringSet("multiselect_key", null);
            for(String s: selections) {
                //Log.wtf(TAG, "value is " + s);
                list += s + " ";
            }
            mMultiSelectListPreference.setSummary("selection  is " + list );
        }
    }

}
