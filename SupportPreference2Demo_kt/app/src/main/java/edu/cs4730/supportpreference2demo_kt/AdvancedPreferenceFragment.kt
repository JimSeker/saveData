package edu.cs4730.supportpreference2demo_kt

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

/**
 * A holder for the advanced preferences xml.
 */
class AdvancedPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferencesadvanced, rootKey)
    }
}
