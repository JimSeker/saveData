package edu.cs4730.sqlitedemo4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * nothing to see here, just a activity that loads a fragment.  See the RecyclerFragment
 * for more information.
 *
 * Note that sqliteDemo and/or ContentProviderRoomDemo are needs to already be installed on the
 *  phone/emulator for this example to work.  It has the content provider used.
 *
 *  sqliteDemo change the to CONTENT_URI in RecylerFragment (for the add) and CussorViewModel
 *  ContentProviderroomDemo use CONTENT_URI2 in RecylerFragment (for the add) and CussorViewModel
 *
 *  Both are listed in the <queires> package section </queires> already and uri_permission is granted in apps already.
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
