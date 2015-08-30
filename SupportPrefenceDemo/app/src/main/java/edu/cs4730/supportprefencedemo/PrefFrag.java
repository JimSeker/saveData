package edu.cs4730.supportprefencedemo;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

/**
 * A simple support preference fragment.   see the xml for most of the data.
 *
 * Note, I'm not sure if the support.v7 is working correctly for embeded preferenceScreen.
 * There example doesn't work. http://developer.android.com/reference/android/support/v7/preference/PreferenceScreen.html
 * uses thetestpref xml files and doesn't do what they say.
 */
public class PrefFrag extends PreferenceFragmentCompat { //} implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
       // addPreferencesFromResource(R.xml.testpref);

        //onNavigateToScreenListener;
    }

    //need to look this statement up and figure it out for embedded preferencescreen.  next doesn't work as is.
    //and I can't find a usage example.
    /*
    One thing you’ll note isn’t in here is preference headers and you’d be totally right. However,
    that doesn’t mean a single list of preferences need to span a 10” tablet screen. Instead, your
    Activity can implement OnPreferenceStartFragmentCallback (http://goo.gl/IZWZBP) to handle
    preferences with an app:fragment attribute or

    OnPreferenceStartScreenCallback (http://goo.gl/CFp5Cr)
    to handle PreferenceScreen preferences. This allows you to construct a ‘header’ style
    PreferenceFragmentCompat in one pane and use those callbacks to replace a second pane without working in
    two separate types of XML files.
     */
/*
    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen) {
        Log.v("Hi", "i'm called.");
       setPreferenceScreen(preferenceScreen);
        return false;
    }
    */
}
