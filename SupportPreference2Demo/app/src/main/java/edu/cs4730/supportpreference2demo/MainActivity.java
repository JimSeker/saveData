package edu.cs4730.supportpreference2demo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import edu.cs4730.supportpreference2demo.databinding.ActivityMainBinding;

/**
 * This is a simple example of using the support library PreferenceFragmentCompat.
 * The MainFragment has a button to launch the SettingsFragment which shows the
 * preferences, which are saved automatically to SharedPreferences.
 * <p>
 * It can't use the Arch Navigation component because it doesn't support
 * PreferenceFragmentCompat (as of this writing anyway 9/10/2025 ).  So the code is little gross in main.
 * <p>
 * It appears M3 has a preference theme built in, so no custom theme is needed like in SupportPreferenceDemo.
 */

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

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
        getSupportFragmentManager().beginTransaction().add(binding.container.getId(), new MainFragment()).commit();
    }

    /**
     * Called when the user has clicked on a preference that has a fragment class name
     * associated with it. The implementation should instantiate and switch to an instance
     * of the given fragment.
     *
     * @param caller The fragment requesting navigation
     * @param pref   The preference requesting the fragment
     * @return {@code true} if the fragment creation has been handled
     */
    @Override
    public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, Preference pref) {

        //take from https://developer.android.com/develop/ui/views/components/settings/organize-your-settings

        // Instantiate the new Fragment.
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
            getClassLoader(), pref.getFragment());
        fragment.setArguments(args);

        // Replace the existing Fragment with the new Fragment.
        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), fragment)
            .addToBackStack(null).commit();
        return true;
    }

    @Override
    public void onFragmentInteraction(int which) {
        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new SettingsFragment()).addToBackStack(null).commit();
    }
}