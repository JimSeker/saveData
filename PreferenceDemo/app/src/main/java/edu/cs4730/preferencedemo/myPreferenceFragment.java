package edu.cs4730.preferencedemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/*
 * this is the simplest version, it just shows the preference screen.
 *   See the xml for preference entries.
 */

public class myPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
