package edu.cs4730.sqlitedemo2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager2 mViewPager;
    TabLayout tabLayout;

    //database columns
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setCurrentItem(7);// set to a specific page in the pager.
        tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout,
            mViewPager,
            new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(mSectionsPagerAdapter.getPageTitle(position));
                }
            }
        ).attach();
    }

    /**
     * A {@link FragmentStateAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new sqlitedemo2Frag();
                case 1:
                    return new loaderDemoFrag();
                default:
                    return new sqlitedemo2Frag();  //can't be null, so return case 0.
            }
        }

        @Override
        public int getItemCount() {
            // Show X total pages.
            return 2;
        }

        //hold over code from viewpager code, but called from the tablayoutmediator.
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "sqlite2 Demo";
                case 1:
                    return "loader Demo";
            }
            return null;
        }
    }
}
