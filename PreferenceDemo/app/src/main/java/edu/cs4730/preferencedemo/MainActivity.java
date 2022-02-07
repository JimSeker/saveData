package edu.cs4730.preferencedemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is showing how to use the standard preference systems.
 * <p>
 * Note: because they are not an androidx fragment, everyone is using "standard" fragments.  This likely
 * a bad idea at this point, and everything should be migrated to support preferences.  See the SupportPreferenceDemo.
 *
 * could instead use this in your app as a seperate activity, that deals with preferences, if you want
 * to continue without supportpreferences in androidx.
 *
 */
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        //setup the mainFragment to show.
        fragmentManager.beginTransaction().replace(R.id.container, new MainFragment()).commit();

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
            transaction.replace(R.id.container, new myPreferenceFragment());
        else
            transaction.replace(R.id.container, new PreferenceupdateFragment());
        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }
}
