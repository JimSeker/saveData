package edu.cs4730.sqlitedemo3;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class RecyclerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int TUTORIAL_LIST_LOADER = 0x01;  //Loader ID number.
    myRecyclerCursorAdapter mAdapter;
    Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
    RecyclerView mRecyclerView;
    Button btn;

    public RecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_recycler, container, false);
        //initialize the loader
        getLoaderManager().initLoader(TUTORIAL_LIST_LOADER, null, this);
        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        //setup the RecyclerView
        mRecyclerView = (RecyclerView) myView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myRecyclerCursorAdapter(getActivity(), null);  //null, since loader handles the cursor.
        //add the adapter to the recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //This button is used to add more data, so the loader will then reload "on it's own".
        btn = (Button) myView.findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(MainActivity.KEY_NAME, "Jesse");
                initialValues.put(MainActivity.KEY_SCORE, "123");
                Uri uri = getActivity().getContentResolver().insert(CONTENT_URI, initialValues);
            }
        });
        return myView;
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
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // called when the data is no longer valid, so remove the cursor
        mAdapter.swapCursor(null);
    }
}
