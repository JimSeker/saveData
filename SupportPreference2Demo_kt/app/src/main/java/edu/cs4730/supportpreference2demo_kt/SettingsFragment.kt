package edu.cs4730.supportpreference2demo_kt

import android.os.Bundle
import android.util.Log
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mEditTextPreference: EditTextPreference
    private lateinit var mListPreference: ListPreference
    private lateinit var mMultiSelectListPreference: MultiSelectListPreference
    private lateinit var mDropDownPreference: DropDownPreference
    private val TAG = "PreferenceupdateFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)


        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.


        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mEditTextPreference = preferenceScreen.findPreference("edittext_preference")!!


        //note we don't use this one, because it's set to create a summary automatically in xml.
        mListPreference = preferenceScreen.findPreference("list_preference")!!
        mMultiSelectListPreference = preferenceManager.findPreference("multiselect_key")!!
        mDropDownPreference = preferenceManager.findPreference("dropdown")!!


        // Setup the initial values using the built in SimpleSummaryProvider
        mListPreference.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance())

        //NO f**king clue here.   This works great in java, but simpleSummaryProvider is not working in kotlin???
        //literally the same code, so I give up, SimpleSummaryProvider doesn't exist in kotlin for some reason..
        //So use the xml way of doing it.
        //mDropDownPreference.setSummaryProvider(DropDownPreference.SimpleSummaryProvider.getInstance())



        //MultiSelectListPreference does not have a SimpleSummaryProvider, so we a custom one.
        mMultiSelectListPreference.setSummaryProvider(
            object : Preference.SummaryProvider<MultiSelectListPreference> {
                override fun provideSummary(preference: MultiSelectListPreference): CharSequence {
                    val list = StringBuilder()
                    val selections = preference.values
                    if (selections.isEmpty()) list.append("No items selected")
                    else {
                        for (s in selections) {
                            Log.wtf(TAG, "value is $s")
                            list.append(s).append(" ")
                        }
                    }
                    return "selection  is $list"
                }
            }
        )


    }

}