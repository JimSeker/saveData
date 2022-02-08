package edu.cs4730.supportprefencedemo_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager = supportFragmentManager
        //setup the mainFragment to show.
        fragmentManager.beginTransaction().add(R.id.container, MainFragment()).commit()
    }

    /**
     * This the callback interface for MainFragment.
     */
    override fun onFragmentInteraction(which: Int) {

        //now change to the SecondFragment, pressing the back button should go to main fragment.
        val transaction = fragmentManager.beginTransaction()
        //Change to the correct fragment for preferences
        if (which == 1)
            transaction.replace(R.id.container, PreferenceupdateFragment())
        else
            transaction.replace(R.id.container, myPreferenceFragment())
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
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = fragmentManager.fragmentFactory.instantiate(classLoader, pref.fragment!!)
        fragment.arguments = args

        //Now replace the existing Fragment with the new Fragment, the listener is doing?? idk.
        fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
            .commit()
        fragmentManager.setFragmentResultListener(
            "reequestKey", this
        ) { requestKey, result ->
            if (requestKey == "requestKey")
                fragmentManager.setFragmentResult(requestKey, result)
        }
        return true
    }
}
