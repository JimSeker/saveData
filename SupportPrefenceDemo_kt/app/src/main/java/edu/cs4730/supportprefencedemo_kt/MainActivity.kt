package edu.cs4730.supportprefencedemo_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import edu.cs4730.supportprefencedemo_kt.databinding.ActivityMainBinding

/**
 * this a redux of the preferenceDemo, but using the androidx.preferencefragmentCompat (prefupdateFrag and prefNewFrag)
 * and v14 prefrencefragment (myPreferenceActivity and prefFrag  since not a support fragment.
 * No there is no need version compatibility, except a couple of v14 specific preferences cant be used before API 14.
 * for all the new preferences xml.
 * <p>
 * maybe useful as well http://developer.android.com/guide/topics/ui/settings.html
 * <p>
 * Big NOT here :
 * Must specify preferenceTheme in theme, it's an item.  see the style.xml file.
 */

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    lateinit var fragmentManager: FragmentManager
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        fragmentManager = supportFragmentManager
        //setup the mainFragment to show.
        fragmentManager.beginTransaction().add(binding.container.id, MainFragment()).commit()
    }

    /**
     * This the callback interface for MainFragment.
     */
    override fun onFragmentInteraction(which: Int) {

        //now change to the SecondFragment, pressing the back button should go to main fragment.
        val transaction = fragmentManager.beginTransaction()
        //Change to the correct fragment for preferences
        if (which == 1) transaction.replace(binding.container.id, PreferenceupdateFragment())
        else transaction.replace(binding.container.id, myPreferenceFragment())
        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null)
        // Commit the transaction
        transaction.commit()
    }

    /**
     * this is "required" as of version 1.1.0 of the androidx.preference.
     * It used when the user taps on preference that calls a preference fragment.
     */
    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat, pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = fragmentManager.fragmentFactory.instantiate(classLoader, pref.fragment!!)
        fragment.arguments = args

        //Now replace the existing Fragment with the new Fragment, the listener is doing?? idk.
        fragmentManager.beginTransaction().replace(binding.container.id, fragment)
            .addToBackStack(null).commit()
        fragmentManager.setFragmentResultListener(
            "reequestKey", this
        ) { requestKey, result ->
            if (requestKey == "requestKey") fragmentManager.setFragmentResult(requestKey, result)
        }
        return true
    }
}
