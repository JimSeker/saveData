package edu.cs4730.sqlitedemo2;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;


public class loaderDemoFrag extends Fragment implements
    LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TUTORIAL_LIST_LOADER = 0x01;  //Loader ID number.
    private SimpleCursorAdapter dataAdapter;
    Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
    ListView lv;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.scorefrag, container, false);
        lv = view.findViewById(R.id.listView1);
        //setup the adapter with a null cursor.
        String[] columns = new String[]{MainActivity.KEY_NAME, MainActivity.KEY_SCORE};
        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.name, R.id.score};

        //initialize the loader
        LoaderManager.getInstance(this).initLoader(TUTORIAL_LIST_LOADER, null, this);
        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
            getActivity().getApplicationContext(), R.layout.scorelist,
            null,   //null now, set in the Loader "Later"
            columns, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        //set the adapter to the listview
        lv.setAdapter(dataAdapter);

        //This button is used to add more data, so the loader will then reload "on it's own".
        btn = view.findViewById(R.id.btn_add);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(MainActivity.KEY_NAME, "Fred");
                initialValues.put(MainActivity.KEY_SCORE, "123");
                Uri uri = getActivity().getContentResolver().insert(CONTENT_URI, initialValues);
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        //setup the information we want for the contentprovider.
        String[] projection = new String[]{MainActivity.KEY_ROWID, MainActivity.KEY_NAME, MainActivity.KEY_SCORE};
        String SortOrder = MainActivity.KEY_SCORE;

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
            CONTENT_URI, projection, null, null, SortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // "later",  once the data has been loaded, now we set the cursor in the adapter
        dataAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // called when the data is no longer valid, so remove the cursor
        dataAdapter.swapCursor(null);
    }

}
