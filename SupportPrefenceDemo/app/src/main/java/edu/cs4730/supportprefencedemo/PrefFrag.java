package edu.cs4730.supportprefencedemo;

import android.os.Bundle;
import androidx.preference.PreferenceFragment;
//import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * A simple support preference fragment.   see the xml for most of the data.
 * Not this is using a v14 preferencefragment (which is not a supportfragment), because I can here.
 * likely if you are using any other support fragment, change this to a v7, so it's a supportfragment as well.
 *
 * This now works with multiple preference screens.  Not it using the set, instead of add
 * with the rootKey, so it knows which screen to show.  The bulk of the code to change between
 * screens in in the activity (myPreferenceActivity).
 * Many thanks to the top answer
 * https://stackoverflow.com/questions/32487206/inner-preferencescreen-not-opens-with-preferencefragmentcompat
 * and their github example code at https://github.com/madlymad/PreferenceApp
 */
public class PrefFrag extends PreferenceFragment  {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);
        //setPreferencesFromResource(R.xml.testpref, rootKey);   //google's example preference.
    }


}
