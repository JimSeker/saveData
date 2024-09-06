package edu.cs4730.contentprosqlitedbdemo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import edu.cs4730.contentprosqlitedbdemo.databinding.FragmentContentProviderAccessBinding;
import edu.cs4730.contentprosqlitedbdemo.db.mySQLiteHelper;

/**
 * Very simple version, that accesses, displays, and add some data to the database via the contentProvider
 */

public class ContentProviderAccessFragment extends Fragment {

    String TAG = "ContentProvider_frag";
    Cursor cursor;
    FragmentContentProviderAccessBinding binding;
    private SimpleCursorAdapter dataAdapter;
    public ContentProviderAccessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContentProviderAccessBinding.inflate(inflater, container, false);

        //get the people URI
        Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
        //setup the information we want for the contentprovider.
        String[] projection = new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE};

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        String SortOrder = mySQLiteHelper.KEY_SCORE;  //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);

        if (cursor == null) {
            Log.i(TAG, "cursor is null...");
        }

        // The desired columns to be bound
        String[] columns = new String[]{mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.name, R.id.score};

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(requireContext(), R.layout.highscore, cursor, columns, to, 0);

        binding.list.setAdapter(dataAdapter);
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Should really create a dialogbox and display all the contact info here.  but I'll get to that
                // when I have time.
                //String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(mySQLiteHelper.KEY_NAME));
                Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show();

            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(mySQLiteHelper.KEY_NAME, "Fred");
                initialValues.put(mySQLiteHelper.KEY_SCORE, "123");
                Uri uri = requireActivity().getContentResolver().insert(CONTENT_URI, initialValues);

                //a view model fixes this and won't be needed.
                cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);
                dataAdapter.swapCursor(cursor);
            }
        });

        return binding.getRoot();
    }

    //a simple method to append information to the Textview
    public void logthis(String item) {
        Log.d(TAG, item);
    }

}