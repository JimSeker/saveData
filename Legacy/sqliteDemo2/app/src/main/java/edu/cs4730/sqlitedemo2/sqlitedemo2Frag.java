package edu.cs4730.sqlitedemo2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

public class sqlitedemo2Frag extends Fragment {

    String TAG = "sqlitedemo2Frag";
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.sqlitedemo2frag, container, false);

        //get the people URI
        Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
        //setup the information we want for the contentprovider.
        String[] projection = new String[]{MainActivity.KEY_ROWID, MainActivity.KEY_NAME, MainActivity.KEY_SCORE};

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        String SortOrder = MainActivity.KEY_SCORE;  //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        ContentResolver cr = requireContext().getContentResolver();
        if (cr == null) {
            Log.wtf(TAG, "contentresolver is null");
        }
        cursor = cr.query(CONTENT_URI, projection, null, null, SortOrder);

        //this is commented out, because better using a listview, which is what displayListView() does.
        //		  if (c.moveToFirst()) {
        //		 	do {
        //		 		String str = "Id: " + c.getString(0);
        //		 		str += "Name: " + c.getString(1);
        //		 	} while (c.moveToNext());
        //		 }
        if (cursor == null) {
            Log.i("CAA", "cursor is null...");
        }

        // The desired columns to be bound
        String[] columns = new String[]{
            MainActivity.KEY_NAME,
            MainActivity.KEY_SCORE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
            R.id.name,
            R.id.score
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
            requireContext(), R.layout.scorelist,
            cursor,
            columns,
            to,
            0);

        ListView listView = (ListView) myView.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                @SuppressLint("Range")
                //String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String name = cursor.getString(cursor.getColumnIndex(MainActivity.KEY_NAME));
                Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show();

            }
        });
        return myView;
    }

}
