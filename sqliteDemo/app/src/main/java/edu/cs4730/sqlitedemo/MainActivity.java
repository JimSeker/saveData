package edu.cs4730.sqlitedemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


/*
 *  Very little to see here.  It sets up the viewpager for the fragments.
 *  The fragment contents the interesting code for databases, cursors, listviews, spinners,
 *  and content providers.
 */

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setCurrentItem(7);// set to a specific page in the pager.
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SqliteDemo_Fragment();
                case 1:
                    return new CursorAdapter_Fragment();
                case 2:
                    return new SpinnerFragment();
                case 3:
                    return new ContentProvider_Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show X total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "sqlite Demo";
                case 1:
                    return "CursorAdapter Demo";
                case 2:
                    return "Spinner Cursor";
                case 3:
                    return "ContentProvider";

            }
            return null;
        }
    }


}
