package edu.cs4730.filesystemdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

/*
 * Example code to demo how to read/write file to local private directory (frag_localp), 
 * local public (frag_localpub) directory and the "sd card"/external media (frag_ext)
 */
public class MainActivity extends ActionBarActivity {
	String TAG = "MainActivity";
	ViewPager viewPager;
	frag_localp one;
	frag_localpub two;
	frag_ext three;
	
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
		int PAGE_COUNT =3;
		
		public myFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			switch (position) {
			  case 0: return one;
			  case 1: return two;
			  case 2: return three;
			  default: return null;
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
		    case 0: return "Local file example";
		    case 1: return "Local file public";
		    case 2: return "External files";
		    default: return "Page "+ String.valueOf(position +1);
		  }   	
        }
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	


}