package edu.cs4730.lvcursordemo;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    BottomNavigationView bnv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bnv);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //At this point, we are doing the same thing that is done for menu selections.
                //if we had a onOptionsItemSelect method for a menu, we could just use it.
                int id = item.getItemId();
                if (id == R.id.simple) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new simple_Fragment()).commit();
                    return true;
                } else if (id == R.id.custom) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new custom_Fragment()).commit();
                    return true;
                } else if (id == R.id.explist) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ExpListview_Fragment()).commit();
                    return true;
                }
                return false;
            }
        });
        if (savedInstanceState == null) {
            //set the first one as the default.
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new simple_Fragment()).commit();
        }
    }

}
