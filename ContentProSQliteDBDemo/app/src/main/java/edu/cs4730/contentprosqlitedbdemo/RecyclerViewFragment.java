package edu.cs4730.contentprosqlitedbdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.cs4730.contentprosqlitedbdemo.db.mySQLiteHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * calling a content provider (local one) and using a recycler view.
 */

public class RecyclerViewFragment extends Fragment {

    RecyclerView mRecyclerView;
    FloatingActionButton fab;
    myAdapter mAdapter;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        //get the people URI
        Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
        //setup the information we want for the contentprovider.
        String[] projection = new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE};

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        String SortOrder = mySQLiteHelper.KEY_SCORE;  //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);


        mRecyclerView = myView.findViewById(R.id.list2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myAdapter(cursor, R.layout.highscore, requireContext());
        //add the adapter to the recyclerview
        mRecyclerView.setAdapter(mAdapter);

        fab = myView.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(mySQLiteHelper.KEY_NAME, "Danny");
                initialValues.put(mySQLiteHelper.KEY_SCORE, "1024");
                Uri uri = requireActivity().getContentResolver().insert(CONTENT_URI, initialValues);

                //a view model fixes this and won't be needed.
                cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);
                mAdapter.setCursor(cursor);
            }
        });

        //with with the Itemlisteners we could easy add delete, click listener is there already, but
        //a modelview would be useful to implement an update feature.

        return myView;
    }
}