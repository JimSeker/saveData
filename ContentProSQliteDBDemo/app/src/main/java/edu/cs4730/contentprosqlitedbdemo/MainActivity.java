package edu.cs4730.contentprosqlitedbdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnItemSelectedListener(
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //setup the fragments here.
                    int id = item.getItemId();
                    if (id == R.id.action_first) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ContentProviderAccessFragment()).commit();
                        item.setChecked(true);
                        return true;
                    } else if (id == R.id.action_second) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new RecyclerViewFragment()).commit();
                        item.setChecked(true);
                    } else if (id == R.id.action_third) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SpinnerFragment()).commit();
                        item.setChecked(true);
                    }

                    return false;
                }

            }

        );
        //start it with the first fragment, if we just started the app.
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ContentProviderAccessFragment()).commit();
    }
}