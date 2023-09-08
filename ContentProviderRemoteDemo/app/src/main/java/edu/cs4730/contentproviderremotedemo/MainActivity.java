package edu.cs4730.contentproviderremotedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.cs4730.contentproviderremotedemo.databinding.ActivityMainBinding;

/**
 * this example connects to a remote (another app's) content provider.  of which is can manage 2 of them.
 * either the ContentProSQliteDBDemo or ContentProviderRoomDemo are needs to already be installed on the
 * phone/emulator for this example to work.  It has the content provider used.
 * <p>
 * Note that sqliteDemo and/or ContentProviderRoomDemo are needs to already be installed on the
 * phone/emulator for this example to work.  It has the content provider used.
 * <p>
 * the CONTENT_URI has two lines, one needs to commented out
 * The first one connects the ContentProSQliteDBDemo contentprovider
 * and the second one connects ContentProviderroomDemo.
 * Both are listed in the <queires> package section </queires> already and uri_permission is granted in apps already.
 */


public class MainActivity extends AppCompatActivity {

    //database columns
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    // for the ContentProSQliteDBDemo use this one.
    public static Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
    // for the ContentProviderroomDemo  use this one.
    //public static Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreroomprovider/score");
    ActivityMainBinding binding;
    FloatingActionButton fab;
    CursorViewModel mViewModel;
    myAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize the View Model
        mViewModel = new ViewModelProvider(this).get(CursorViewModel.class);

        //setup the RecyclerView
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new myAdapter(this, mViewModel);  //null, since ModelView and LiveData will handle it.
        //add the adapter to the recyclerview
        binding.list.setAdapter(mAdapter);

        //This button is used to add more data, so the loader will then reload "on it's own".
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(MainActivity.KEY_NAME, "Jim");
                initialValues.put(MainActivity.KEY_SCORE, "3012");
                Uri uri = getContentResolver().insert(CONTENT_URI, initialValues);

            }
        });


    }
}