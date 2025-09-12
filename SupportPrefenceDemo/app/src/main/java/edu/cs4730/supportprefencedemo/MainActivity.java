package edu.cs4730.supportprefencedemo;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import android.os.Bundle;

import edu.cs4730.supportprefencedemo.databinding.ActivityMainBinding;


/**
 * This is a simple example of using the support library PreferenceFragmentCompat.
 * The MainFragment has a button to launch the SettingsFragment which shows the
 * preferences, which are saved automatically to SharedPreferences.  This showing older methods of
 * display summary information.  Supportpreference2demo is a newer methods.
 * <p>
 * It can't use the Arch Navigation component because it doesn't support
 * PreferenceFragmentCompat (as of this writing anyway 9/10/2025 ).  So the code is little gross in main.
 * <p>
 * Big NOT here :
 * Must specify preferenceTheme in theme, it's an item.  see the style.xml file.
 */
public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    FragmentManager fragmentManager;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.container, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        fragmentManager = getSupportFragmentManager();
        //setup the mainFragment to show.
        fragmentManager.beginTransaction().add(binding.container.getId(), new MainFragment()).commit();
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
            transaction.replace(binding.container.getId(), new PreferenceupdateFragment());
        else
            transaction.replace(binding.container.getId(), new myPreferenceFragment());
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
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = fragmentManager.getFragmentFactory().instantiate(
            getClassLoader(), pref.getFragment());
        fragment.setArguments(args);

        //Now replace the existing Fragment with the new Fragment, the listener is doing?? idk.
        fragmentManager.beginTransaction().replace(binding.container.getId(), fragment).addToBackStack(null).commit();
        fragmentManager.setFragmentResultListener("reequestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals("requestKey"))
                    fragmentManager.setFragmentResult(requestKey, result);
            }
        });
        return true;

    }
}
