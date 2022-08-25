package edu.cs4730.sqlitedemo3;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This an implementation of a cursor and recyclerview with card view.
 * There is no such things as a cursor adapter for the recyclerview, so I got code from
 * another person to handle of the pieces and loaders.
 * CursorRecyclerAdapter.java was Created by skywin and found at https://gist.github.com/Shywim/127f207e7248fe48400b
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TUTORIAL_LIST_LOADER = 0x01;  //Loader ID number.
    myRecyclerCursorAdapter mAdapter;
    Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
    RecyclerView mRecyclerView;
    Button btn;

    //database columns
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager.getInstance(this).initLoader(TUTORIAL_LIST_LOADER, null, this);
        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information to setup the recyclerview.
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myRecyclerCursorAdapter(this, null);  //null, since loader handles the cursor.
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
                Uri uri = getContentResolver().insert(CONTENT_URI, initialValues);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //setup the information we want for the contentprovider.
        String[] projection = new String[]{MainActivity.KEY_ROWID, MainActivity.KEY_NAME, MainActivity.KEY_SCORE};
        String SortOrder = MainActivity.KEY_SCORE;

        CursorLoader cursorLoader = new CursorLoader(this,
            CONTENT_URI, projection, null, null, SortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // "later",  once the data has been loaded, now we set the cursor in the adapter
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // called when the data is no longer valid, so remove the cursor
        mAdapter.swapCursor(null);
    }

}
