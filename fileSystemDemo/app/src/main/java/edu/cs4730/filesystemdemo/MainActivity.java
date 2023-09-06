package edu.cs4730.filesystemdemo;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.cs4730.filesystemdemo.databinding.ActivityMainBinding;

/**
 * Example code to demo how to read/write file to local private directory (localPrivate_Fragment),
 * local public (localPublic_Fragment) directory
 */
public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    localPrivate_Fragment one;
    localPublic_Fragment two;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        one = new localPrivate_Fragment();
        two = new localPublic_Fragment();

        binding.bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //At this point, we are doing the same thing that is done for menu selections.
                //if we had a onOptionsItemSelect method for a menu, we could just use it.
                int id = item.getItemId();
                if (id == R.id.one) {
                    getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), one).commit();
                    return true;
                } else if (id == R.id.two) {
                    getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), two).commit();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            //set the first one as the default.
            getSupportFragmentManager().beginTransaction().add(binding.container.getId(), one).commit();
        }
    }

}
