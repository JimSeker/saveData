package edu.cs4730.contentprodemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    String TAG = "MainActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    return new ContactsDemo_Fragment();
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
