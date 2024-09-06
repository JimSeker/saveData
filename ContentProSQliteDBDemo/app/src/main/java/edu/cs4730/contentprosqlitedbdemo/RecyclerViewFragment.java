package edu.cs4730.contentprosqlitedbdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import edu.cs4730.contentprosqlitedbdemo.databinding.FragmentRecyclerViewBinding;
import edu.cs4730.contentprosqlitedbdemo.db.mySQLiteHelper;

/**
 * calling a content provider (local one) and using a recycler view.
 */

public class RecyclerViewFragment extends Fragment {
    FragmentRecyclerViewBinding binding;
    myAdapter mAdapter;
    Cursor cursor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false);

        //get the people URI
        Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
        //setup the information we want for the contentprovider.
        String[] projection = new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE};

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        String SortOrder = mySQLiteHelper.KEY_SCORE;  //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);

        binding.list2.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.list2.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myAdapter(cursor, R.layout.highscore, requireContext());
        //add the adapter to the recyclerview
        binding.list2.setAdapter(mAdapter);

        binding.fab2.setOnClickListener(new View.OnClickListener() {
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

        return binding.getRoot();
    }
}