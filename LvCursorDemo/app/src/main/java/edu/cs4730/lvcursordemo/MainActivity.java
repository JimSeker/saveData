package edu.cs4730.lvcursordemo;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.cs4730.lvcursordemo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
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

        binding.bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //At this point, we are doing the same thing that is done for menu selections.
                //if we had a onOptionsItemSelect method for a menu, we could just use it.
                int id = item.getItemId();
                if (id == R.id.simple) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(binding.container.getId(), new simple_Fragment()).commit();
                    return true;
                } else if (id == R.id.custom) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(binding.container.getId(), new custom_Fragment()).commit();
                    return true;
                } else if (id == R.id.explist) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(binding.container.getId(), new ExpListview_Fragment()).commit();
                    return true;
                }
                return false;
            }
        });
        if (savedInstanceState == null) {
            //set the first one as the default.
            getSupportFragmentManager().beginTransaction()
                .add(binding.container.getId(), new simple_Fragment()).commit();
        }
    }

}
