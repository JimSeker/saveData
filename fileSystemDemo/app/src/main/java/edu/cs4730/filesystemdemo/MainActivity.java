package edu.cs4730.filesystemdemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/*
 * Example code to demo how to read/write file to local private directory (frag_localp), 
 * local public (frag_localpub) directory and the "sd card"/external media (frag_ext)
 *
 * We need to ask the user for read/write permission for the external reads and writes (frag_ext).
 */
public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    ViewPager viewPager;
    frag_localp one;
    frag_localpub two;
    frag_ext three;

    public static final int REQUEST_PERM_ACCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        one = new frag_localp();
        two = new frag_localpub();
        three = new frag_ext();

        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new myFragmentPagerAdapter(fragmentManager));

    }


    public class myFragmentPagerAdapter extends FragmentPagerAdapter {
        int PAGE_COUNT = 3;

        public myFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return one;
                case 1:
                    return two;
                case 2:
                    return three;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return PAGE_COUNT;
        }

        //getPageTitle required for the PageStripe to work and have a value.
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Local file example";
                case 1:
                    return "Local file public";
                case 2:
                    return "External files";
                default:
                    return "Page " + String.valueOf(position + 1);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //handle the response.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERM_ACCESS: {  //external file write fragment.
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    three.logger.append("External File Write Access: Granted");
                    three.extfolder();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    three.logger.append("External File Write Access: Not Granted");
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
