package edu.cs4730.preferencedemo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


/*
 * http://stackoverflow.com/questions/7112706/preferencefragment-alternative-for-the-android-compatibility-api
 * http://android-er.blogspot.com/2012/07/example-of-using-preferencefragment.html
 * http://developer.android.com/reference/android/preference/PreferenceFragment.html
 * http://developer.android.com/reference/android/preference/PreferenceActivity.html
 * http://www.cs.dartmouth.edu/~campbell/cs65/lecture12/lecture12.html
 * http://gmariotti.blogspot.com/2013/01/preferenceactivity-preferencefragment.html
 *
 * This a demo of how to setup perferences screen for both 2.3.3 and below and 3.0+ and above
 * There is not a support preference fragment, so this all has to be done manually.  In
 * a number of places is a check to see what api version we are using, so it can use a preferenceActivity which deprecated
 * or preferenceFragment (old way or new way).
 *
 */
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        findViewById(R.id.button1).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), myPreferenceActivity.class));
			}
        });
		
        findViewById(R.id.button2).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), PrefupdateActivity.class));
			}
        });
	}

    @Override
    protected void onResume() {
    	super.onResume(); 
    	getPrefs();
    }
    /*
     * Get the preferences for the game
     */
    void getPrefs() {
    	boolean useSensor, useSwipe;
    	//Toast.makeText(getApplicationContext(), "Get prefs", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        useSensor =  prefs.getBoolean("sensorPref", false);
    	useSwipe = prefs.getBoolean("swipePref", true);
    	Toast.makeText(getApplicationContext(), "Sensor is " + useSensor, Toast.LENGTH_SHORT).show();
    	String text = prefs.getString("textPref", "");
    	Toast.makeText(getApplicationContext(), "Text is  " + text, Toast.LENGTH_SHORT).show();
    	String list = prefs.getString("list_preference","");
    	Toast.makeText(getApplicationContext(), "List " + list, Toast.LENGTH_SHORT).show();
    }
    
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
