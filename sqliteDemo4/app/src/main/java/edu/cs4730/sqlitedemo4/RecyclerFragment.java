package edu.cs4730.sqlitedemo4;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This an implementation of a cursor and recyclerview with card view.
 * There is no such things as a cursor adapter for the recyclerview, so I got code from
 * another person to handle of the pieces to handler observers.
 * CursorRecyclerAdapter.java was Created by skywin and found at https://gist.github.com/Shywim/127f207e7248fe48400b
 * <p>
 * Changed to using ModelView and LiveData, instead of loaders.  Since his code using an observer, it's mostly easy change.
 */
public class RecyclerFragment extends Fragment {


    myRecyclerCursorAdapter mAdapter;
    public static Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
    public static final Uri CONTENT_URI2 = Uri.parse("content://edu.cs4730.scoreroomprovider/score");
    RecyclerView mRecyclerView;
    Button btn;

    CursorViewModel mViewModel;

    public RecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_recycler, container, false);

        //initialize the View Model
        mViewModel = new ViewModelProvider(getActivity()).get(CursorViewModel.class);

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        //setup the RecyclerView
        mRecyclerView = myView.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myRecyclerCursorAdapter(getActivity(), null);  //null, since ModelView and LiveData will handle it.
        //add the adapter to the recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //This button is used to add more data, so the loader will then reload "on it's own".
        btn = myView.findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(MainActivity.KEY_NAME, "Jesse");
                initialValues.put(MainActivity.KEY_SCORE, "123");
                //for sqldemo use this one
                Uri uri = getActivity().getContentResolver().insert(CONTENT_URI, initialValues);
                //contenProviderRoomDemo use this one
                //Uri uri = getActivity().getContentResolver().insert(CONTENT_URI2, initialValues);
            }
        });

        //Note the observer is a Cursor (not a ContentProviderLiveData, because it setValue/postValue is a cursor.
        // So observer matches that.
        mViewModel.getData().observe(getActivity(), new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor data) {
                mAdapter.swapCursor(data);
            }
        });
        return myView;
    }
}