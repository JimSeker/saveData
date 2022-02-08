package edu.cs4730.supportprefencedemo_kt

import androidx.preference.PreferenceFragmentCompat
import android.os.Bundle

/**
 * A holder for the advanced preferences xml.
 */
class AdvancedPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencesadvanced, rootKey)
    }
}