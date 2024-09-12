package edu.cs4730.contentprodemo;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

import edu.cs4730.contentprodemo.databinding.ActivityMainBinding;

/**
 * Example setup bottomnavview for two fragements
 * This demo's a contact contentprovider
 * and a custom content provider (called dummyCP).
 *   see the two fragments for how they work.
 */

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    ActivityMainBinding binding;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navView.setOnItemSelectedListener(
            new NavigationBarView.OnItemSelectedListener()  {
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.action_CP) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(binding.container.getId(), new Contentp_Fragment()).commit();
                        return true;

                    } else if (item.getItemId() == R.id.action_contact) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(binding.container.getId(), new ContactsDemo_Fragment()).commit();
                        return true;
                    } else
                        return false;
                }
            }
        );
        //set the first one as the default.
        getSupportFragmentManager().beginTransaction()
            .add(binding.container.getId(), new Contentp_Fragment()).commit();
    }

}
