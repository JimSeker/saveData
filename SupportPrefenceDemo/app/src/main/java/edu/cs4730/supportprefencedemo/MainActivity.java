package edu.cs4730.supportprefencedemo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;


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
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        //setup the mainFragment to show.
        fragmentManager.beginTransaction().add(R.id.container, new MainFragment()).commit();
    }

    /**
     * This the callback interface for MainFragment.
     */
    @Override
    public void onFragmentInteraction(int which) {

        //now change to the SecondFragment, pressing the back button should go to main fragment.
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //Change to the correct fragment for preferences
        if (which == 1)
            transaction.replace(R.id.container, new PreferenceupdateFragment());
        else
            transaction.replace(R.id.container, new myPreferenceFragment());
        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * this is "required" as of version 1.1.0 of the androidx.preference.
     * It used when the user taps on preference that calls a preference fragment.
     */
    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = fragmentManager.getFragmentFactory().instantiate(
            getClassLoader(),
            pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit();
        return true;

    }
}
