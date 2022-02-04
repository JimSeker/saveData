package edu.cs4730.sqlitedemo4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * nothing to see here, just a activity that loads a fragment.  See the RecyclerFragment
 * for more information.
 * <p>
 * Note that sqliteDemo and/or ContentProviderRoomDemo are needs to already be installed on the
 * phone/emulator for this example to work.  It has the content provider used.
 * <p>
 * sqliteDemo change the to CONTENT_URI in RecylerFragment (for the add) and CussorViewModel
 * ContentProviderroomDemo use CONTENT_URI2 in RecylerFragment (for the add) and CussorViewModel
 * <p>
 * Both are listed in the <queires> package section </queires> already and uri_permission is granted in apps already.
 */

public class MainActivity extends AppCompatActivity {
    //database columns
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    myRecyclerCursorAdapter mAdapter;
    public static Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
    public static final Uri CONTENT_URI2 = Uri.parse("content://edu.cs4730.scoreroomprovider/score");
    RecyclerView mRecyclerView;
    Button btn;
    CursorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize the View Model
        mViewModel = new ViewModelProvider(this).get(CursorViewModel.class);

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        //setup the RecyclerView
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myRecyclerCursorAdapter(this, null);  //null, since ModelView and LiveData will handle it.
        //add the adapter to the recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //This button is used to add more data, so the loader will then reload "on it's own".
        btn = findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(MainActivity.KEY_NAME, "Jesse");
                initialValues.put(MainActivity.KEY_SCORE, "123");
                //for sqldemo use this one
                Uri uri = getContentResolver().insert(CONTENT_URI, initialValues);
                //contenProviderRoomDemo use this one
                //Uri uri = getActivity().getContentResolver().insert(CONTENT_URI2, initialValues);
            }
        });

        //Note the observer is a Cursor (not a ContentProviderLiveData, because it setValue/postValue is a cursor.
        // So observer matches that.
        mViewModel.getData().observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor data) {
                mAdapter.swapCursor(data);
            }
        });
    }
}
