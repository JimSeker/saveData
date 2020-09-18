package edu.cs4730.filesystemdemo;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Example code to demo how to read/write file to local private directory (localPrivate_Fragment),
 * local public (localPublic_Fragment) directory
 */
public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    localPrivate_Fragment one;
    localPublic_Fragment two;
    BottomNavigationView bnv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        one = new localPrivate_Fragment();
        two = new localPublic_Fragment();

        bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //At this point, we are doing the same thing that is done for menu selections.
                //if we had a onOptionsItemSelect method for a menu, we could just use it.
                int id = item.getItemId();
                if (id == R.id.one) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, one).commit();
                    return true;
                } else if (id == R.id.two) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, two).commit();
                    return true;
                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            //set the first one as the default.
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, one).commit();
        }
    }

}
