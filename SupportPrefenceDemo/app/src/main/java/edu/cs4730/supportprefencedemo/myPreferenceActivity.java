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


}
