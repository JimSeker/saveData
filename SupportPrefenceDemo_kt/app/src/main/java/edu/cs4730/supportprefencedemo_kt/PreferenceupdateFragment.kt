package edu.cs4730.supportprefencedemo_kt

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.*

/**
 * The support version of the preference fragment.
 */
class PreferenceupdateFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    private lateinit var mEditTextPreference: EditTextPreference
    private lateinit var mListPreference: ListPreference
    private lateinit var mMultiSelectListPreference: MultiSelectListPreference
    private lateinit var mDropDownPreference: DropDownPreference
    private val TAG = "PreferenceupdateFragment"

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mEditTextPreference = preferenceScreen.findPreference("edittext_preference")!!
        mListPreference = preferenceScreen.findPreference("list_preference")!!
        mMultiSelectListPreference = preferenceManager.findPreference("multiselect_key")!!
        mDropDownPreference = preferenceManager.findPreference("dropdown")!!
    }

    override fun onResume() {
        super.onResume()

        // Setup the initial values
        mEditTextPreference.summary = "Text is " + mEditTextPreference.sharedPreferences!!
            .getString("edittext_preference", "Default")
        mListPreference.summary = "Current value is " + mListPreference.sharedPreferences!!
            .getString("list_preference", "Default")
        // multiselect returns a array set, not a string, so create one.
        var list = ""
        val selections = mMultiSelectListPreference.sharedPreferences!!
            .getStringSet("multiselect_key", null)
        for (s in selections!!) {
            Log.wtf(TAG, "value is $s")
            list += "$s "
        }
        mMultiSelectListPreference.summary = "selection  is $list"
        mDropDownPreference.summary =
            "Current value is " + mDropDownPreference.sharedPreferences!!
                .getString("dropdown", "Default")

        // Set up a listener whenever a key changes
        preferenceScreen.sharedPreferences!!.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        // Unregister the listener whenever a key changes
        preferenceScreen.sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "edittext_preference") {  //where textPref is the key used in the xml.
            mEditTextPreference.summary =
                "Text is " + sharedPreferences.getString("edittext_preference", "Default")
        } else if (key == "list_preference") {
            mListPreference.summary =
                "Current value is " + sharedPreferences.getString(key, "Default")
        } else if (key == "multiselect_key") {
            var list = ""
            val selections = mMultiSelectListPreference.sharedPreferences!!
                .getStringSet("multiselect_key", null)
            for (s in selections!!) {
                //Log.wtf(TAG, "value is " + s);
                list += "$s "
            }
            mMultiSelectListPreference.summary = "selection  is $list"
        } else if (key == "dropdown") {
            mDropDownPreference.summary =
                "Current value is " + mDropDownPreference.sharedPreferences!!
                    .getString("dropdown", "Default")
        }
    }
}