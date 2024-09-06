package edu.cs4730.contentprodemo;

import androidx.annotation.NonNull;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
    public static final int REQUEST_READ_CONTACTS = 0;

    ActivityMainBinding binding;;
    ContactsDemo_Fragment myContactsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //for permissions and BottomNavigationView.
        myContactsFrag = new ContactsDemo_Fragment();

        binding.navView.setOnItemSelectedListener(
            new NavigationBarView.OnItemSelectedListener()  {
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.action_CP) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(binding.container.getId(), new Contentp_Fragment()).commit();
                        return true;

                    } else if (item.getItemId() == R.id.action_contact) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(binding.container.getId(), myContactsFrag).commit();
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

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        Log.v(TAG, "onRequest result called.");
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                //received result for GPS access
                Log.v(TAG, "Received response for contacts permission request.");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.v(TAG, permissions[0] + " permission has now been granted. Showing preview.");
                    Toast.makeText(this, "access to Contacts granted", Toast.LENGTH_SHORT).show();
                    myContactsFrag.setupContactsList(); //setup the demo now.
                } else {
                    // permission denied,    Disable this feature or close the app.
                    Log.v(TAG, "Contacts permission was NOT granted.");
                    Toast.makeText(this, "Contacts access NOT granted", Toast.LENGTH_SHORT).show();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
