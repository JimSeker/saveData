package edu.cs4730.sqlitedemo;

import android.content.Intent;
import android.net.Uri;
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

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;


/**
 * Very little to see here.  It sets up the viewpager for the fragments.
 * The fragment contents the interesting code for databases, cursors, listviews, spinners,
 * and content providers.
 */

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager2 mViewPager;
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        //thi shows how to give permissions to an app, but this app also has  android:grantUriPermissions="true" in the manifest.
        //but started in API 30, the calling package also need in manifest.xml, a <queries> <package android:name="edu.cs4730.sqlitedemo" /> </queries> for it to work

        //grantUriPermission("edu.cs4730.sqlitedemo2", Uri.parse("content://edu.cs4730.scoreprovider/"), FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission("edu.cs4730.sqlitedemo3", Uri.parse("content://edu.cs4730.scoreprovider/"), FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //grantUriPermission("edu.cs4730.sqlitedemo4", Uri.parse("content://edu.cs4730.scoreprovider/"), FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
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
                    return new SqliteDemo_Fragment();
            }
        }

        @Override
        public int getItemCount() {
            // Show X total pages.
            return 4;
        }

        //hold over code from viewpager code, but called from the tablayoutmediator.
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "sqlite Demo";
                case 1:
                    return "CursorAdapter Demo";
                case 2:
                    return "Spinner Cursor";
                case 3:
                    return "Content Provider";

            }
            return null;
        }
    }


}
