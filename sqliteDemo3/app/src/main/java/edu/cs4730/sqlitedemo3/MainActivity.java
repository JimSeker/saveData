package edu.cs4730.sqlitedemo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
  * nothing to see here, just a activity that loads a fragment.  See the RecyclerFragment
  * for more information.
  *
  * Note that sqliteDemo needs to already be installed on the phone/emulator for this example
  * to work.  It has the content provider used.
 */

public class MainActivity extends AppCompatActivity {
    //database columns
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RecyclerFragment())
                    .commit();
        }
    }



}
