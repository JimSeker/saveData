package edu.cs4730.supportprefencedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class myPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefFrag()).commit();
    }

    //need to look this statement up and figure it out for embedded preferencescreen.  next doesn't work as is.
    /*
    One thing you’ll note isn’t in here is preference headers and you’d be totally right. However,
    that doesn’t mean a single list of preferences need to span a 10” tablet screen. Instead, your
    Activity can implement OnPreferenceStartFragmentCallback (http://goo.gl/IZWZBP) to handle
    preferences with an app:fragment attribute or OnPreferenceStartScreenCallback (http://goo.gl/CFp5Cr)
    to handle PreferenceScreen preferences. This allows you to construct a ‘header’ style
    PreferenceFragmentCompat in one pane and use those callbacks to replace a second pane without working in
    two separate types of XML files.
     */

}
