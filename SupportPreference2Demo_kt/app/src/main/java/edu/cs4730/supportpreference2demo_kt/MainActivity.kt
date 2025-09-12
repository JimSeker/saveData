package edu.cs4730.supportpreference2demo_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import edu.cs4730.supportpreference2demo_kt.databinding.ActivityMainBinding

/**
 * This is a simple demo of the AndroidX Preference library, which is the replacement for
 * the deprecated Android Preference library.  This demo is similar to the original demo,
 * but uses only fragments, not activities.  So everything is done in MainActivity and
 * fragments.
 *
 * Note: The design support library version of preference is no longer being maintained.
 * Google suggests using the AndroidX Preference library instead, which is what this demo
 * shows.
 * 
 * lastly, Arch Navigation component can be used with preference as well, since it doesn't support
 * the PreferenceFragmentCompat.OnPreferenceStartFragmentCallback interface. So the code is little gross.
 *
 * Note: this example is the same as the java version, just in kotlin.  Except for one weird thing
 * in SettingsFragment, I give up on DropDownPreference SimpleSummaryProvider is broken in kotlin for some reason.
 */

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        supportFragmentManager.beginTransaction()
            .add(binding.container.getId(), MainFragment()).commit()
    }

    /**
     * Called when the user has clicked on a preference that has a fragment class name
     * associated with it. The implementation should instantiate and switch to an instance
     * of the given fragment.
     *
     * @param caller The fragment requesting navigation
     * @param pref   The preference requesting the fragment
     * @return `true` if the fragment creation has been handled
     */
    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        //take from https://developer.android.com/develop/ui/views/components/settings/organize-your-settings

        val args = pref.getExtras()
        val fragment = supportFragmentManager.getFragmentFactory().instantiate(
            classLoader, pref.fragment!!
        )
        fragment.setArguments(args)

        // Replace the existing Fragment with the new Fragment.
        supportFragmentManager.beginTransaction().replace(binding.container.getId(), fragment)
            .addToBackStack(null).commit()
        return true
    }

    override fun onFragmentInteraction(which: Int) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.getId(), SettingsFragment()).addToBackStack(null).commit()
    }

}