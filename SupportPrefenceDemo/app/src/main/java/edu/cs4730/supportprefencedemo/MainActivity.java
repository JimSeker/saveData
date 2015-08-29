package edu.cs4730.supportprefencedemo;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/*
 * this a redux of the preferenceDemo, but using the support.v7.preferencefragmentCompat.
 * gets rid of all that extra bs of trying to figure out versions of code.  plus allows
 * for all the new preferences xml.
 *
 * Note not all of the new xml preferences are implemented yet.
 * I'll all add a new preference xml for them.  while adding support for elements such as SwitchPreference
 *      (previously only available on API 14+ devices) to all API 7+ devices.
 *
  * Big NOT here :
 * Must specify preferenceTheme in theme, it's an item.  see the style.xml file.
         or get this error:   at android.support.v7.preference.PreferenceFragmentCompat.onCreate(PreferenceFragmentCompat.java:202)
 */
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        //setup the mainFragment to show.
        fragmentManager.beginTransaction().add(R.id.container, new MainFragment()).commit();


    }
    /*
     * This the callback interface for MainFragment.
     */
    @Override
    public void onFragmentInteraction() {

        //now change to the SecondFragment, pressing the back button should go to main fragment.
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        //remove firstfragment from the stack and replace it with two.
        transaction.replace(R.id.container,new PrefupdateFrag());
        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }


}
