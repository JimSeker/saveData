package edu.cs4730.contentprodemo;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    public static final int REQUEST_READ_CONTACTS = 0;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ContactsDemo_Fragment myContactsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for premissions and viewpager
        myContactsFrag = new ContactsDemo_Fragment();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setCurrentItem(7);// set to a specific page in the pager.
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    /**
     * Simple viewpager to display the contacts fragment and contentp for the dummy content provider
     * in this project.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Contentp_Fragment();
                case 1:
                    return myContactsFrag;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show X total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Content Provider Demo";
                case 1:
                    return "Contacts Demo";
            }
            return null;
        }
    }


}
