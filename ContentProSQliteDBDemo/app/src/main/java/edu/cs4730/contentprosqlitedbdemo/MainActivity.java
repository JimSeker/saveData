package edu.cs4730.contentprosqlitedbdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.cs4730.contentprosqlitedbdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        binding.navView.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //setup the fragments here.
                    int id = item.getItemId();
                    if (id == R.id.action_first) {
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new ContentProviderAccessFragment()).commit();
                        item.setChecked(true);
                        return true;
                    } else if (id == R.id.action_second) {
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new RecyclerViewFragment()).commit();
                        item.setChecked(true);
                    } else if (id == R.id.action_third) {
                        getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new SpinnerFragment()).commit();
                        item.setChecked(true);
                    }

                    return false;
                }

            }

        );
        //start it with the first fragment, if we just started the app.
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(binding.container.getId(), new ContentProviderAccessFragment()).commit();
    }
}