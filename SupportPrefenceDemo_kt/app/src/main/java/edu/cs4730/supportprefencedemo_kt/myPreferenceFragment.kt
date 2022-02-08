package edu.cs4730.supportprefencedemo_kt

import androidx.preference.PreferenceFragmentCompat
import android.os.Bundle

/**
 * A simple support preference fragment.   see the xml for most of the data.
 *
 * This now works with multiple preference screens.  Not it using the set, instead of add
 * with the rootKey, so it knows which screen to show.  The bulk of the code to change between
 * screens in in the activity (myPreferenceActivity).
 * Many thanks to the top answer
 * https://stackoverflow.com/questions/32487206/inner-preferencescreen-not-opens-with-preferencefragmentcompat
 * and their github example code at https://github.com/madlymad/PreferenceApp
 */
class myPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}