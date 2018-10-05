package edu.cs4730.supportprefencedemo;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/**
 * this a redux of the preferenceDemo, but using the support.v7.preferencefragmentCompat (prefupdateFrag and prefNewFrag)
 * and v14 prefrencefragment (myPreferenceActivity and prefFrag  since not a support fragment.
 * No there is no need version compatibility, except a couple of v14 specific preferences cant be used before API 14.
 * for all the new preferences xml.
 * <p>
 * maybe useful as well http://developer.android.com/guide/topics/ui/settings.html
 * <p>
 * I'll all add a new preference xml for them.  while adding support for elements such as SwitchPreference
 * (previously only available on API 14+ devices) to all API 7+ devices.
 * <p>
 * Big NOT here :
 * Must specify preferenceTheme in theme, it's an item.  see the style.xml file.
 * or get this error:   at android.support.v7.preference.PreferenceFragmentCompat.onCreate(PreferenceFragmentCompat.java:202)
 */
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {
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
        //Change to the correct fragment for perferences
        if (which == 1)
            transaction.replace(R.id.container, new PreferenceupdateFragment());
        else
            transaction.replace(R.id.container, new myPreferenceFragment());
        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
