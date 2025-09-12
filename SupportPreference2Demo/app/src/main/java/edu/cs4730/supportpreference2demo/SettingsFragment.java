package edu.cs4730.supportpreference2demo;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DropDownPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

/**
 * Shows how to use the various preferences, and how to update the summary line with the current value.
 * Note that EditTextPreference, ListPreference and DropDownPreference have a built in SummaryProvider
 * that can be used instead of the code here.  See the preferences.xml edittext_preference for an example xml.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private EditTextPreference mEditTextPreference;
    private ListPreference mListPreference;
    private MultiSelectListPreference mMultiSelectListPreference;
    private DropDownPreference mDropDownPreference;
    private String TAG = "PreferenceupdateFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //NOTE the rest of this code in not necessary, used so you can display current value on the summary line.

        // Get a reference to the preferences, so we can dynamically update the preference screen summary info.
        mEditTextPreference = getPreferenceScreen().findPreference("edittext_preference");
        //note we don't use this one, because it's set to create a summary automatically in xml.


        mListPreference = getPreferenceScreen().findPreference("list_preference");
        mMultiSelectListPreference = getPreferenceManager().findPreference("multiselect_key");
        mDropDownPreference = getPreferenceManager().findPreference("dropdown");

        // Setup the initial values using the built in SimpleSummaryProvider
        mListPreference.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        mDropDownPreference.setSummaryProvider(DropDownPreference.SimpleSummaryProvider.getInstance());

        //MultiSelectListPreference does not have a SimpleSummaryProvider, so we a custom one.
        mMultiSelectListPreference.setSummaryProvider(
            new Preference.SummaryProvider<MultiSelectListPreference>() {
                @NonNull
                @Override
                public CharSequence provideSummary(@NonNull MultiSelectListPreference preference) {
                    StringBuilder list = new StringBuilder();
                    Set<String> selections = preference.getValues();
                    if (selections.isEmpty())
                        list.append("No items selected");
                    else {
                        for (String s : selections) {
                            Log.wtf(TAG, "value is " + s);
                            list.append(s).append(" ");
                        }
                    }
                    return "selection  is " + list;
                }
            }
        );
    }
}