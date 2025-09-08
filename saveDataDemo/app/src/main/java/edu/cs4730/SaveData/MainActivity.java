package edu.cs4730.SaveData;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.cs4730.SaveData.databinding.ActivityMainBinding;


/**
 * This example shows the difference with a viewmodel, sharedpeferences and instance bundle.
 * the shared preference system to store data, and viewmodel for when the app is rotated, and
 * for long term vs short term data storage.
 */

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    int b1 = 0, b2 = 0, b3 = 0;
    DataViewModel mViewModel;

    ActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        binding.tvNothing.setText(String.valueOf(b1));

        //bundle method

        if (savedInstanceState != null) { //There is saved data
            logthis("There is data, restoring");
            b2 = savedInstanceState.getInt("b2", 0);  //default in case of issues.
            binding.tvBundle.setText(String.valueOf(b2));
        } else {
            logthis("No data in savedInstanceState");
        }

        //preference method
        //settext handled in getprefs();
        getprefs();

        //lastly the model view
        //for the model view live variable.
        mViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        mViewModel.getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer data) {
                logthis("Data changed, updating!");
                binding.tvViewmodel.setText(data.toString());
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increment each number.  over rotations and starts, these numbers will be different.
                b1++;
                binding.tvNothing.setText(String.valueOf(b1));
                b2++;
                binding.tvBundle.setText(String.valueOf(b2));
                b3++;
                binding.tvPreference.setText(String.valueOf(b3));

                mViewModel.increment();  //settext is handled by the observer.
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        logthis("OnPause called");
        // Store values between instances here
        SharedPreferences preferences = getSharedPreferences("example", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //store b3 in preferences
        editor.putInt("b3", b3);
        editor.apply();
        logthis("Stored preferences");
    }

    @Override
    public void onResume() {
        super.onResume();
        logthis("OnResume called");
        getprefs();
    }

    /**
     * getpres() allows me to get the sharePreferences code in on place, it is called from
     * onCreate and onPause.
     */
    void getprefs() {
        logthis("Restoring preferences.");
        // Get the between instance stored values
        SharedPreferences preferences = getSharedPreferences("example", MODE_PRIVATE);
        //get the key d3 and set a default value of "" if the key doesn't exist.  IE the first time this app is run.
        b3 = preferences.getInt("b3", 0);
        binding.tvPreference.setText(String.valueOf(b3));
    }

    /**
     * Called, when app is being destroyed, but maybe called after onStop as well.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the state et_bundle/b2
        outState.putInt("b2", b2);
        super.onSaveInstanceState(outState);
    }

    /**
     * simple method to add the log TextView.
     */
    public void logthis(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            binding.log.append(newinfo + "\n");
            Log.d(TAG, newinfo);
        }
    }
}