package edu.cs4730.sqlitedemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import edu.cs4730.sqlitedemo.db.mySQLiteHelper;

/**
 * This is a demo of how to use a content provider (which is in this code, myDBContenProvider is the provider)
 *  with a listview.   This is very similar to CursorAdapter_fragment,
  *
  *  See sqliteDemo3 for a recyclerview using a content provider.
 */

public class ContentProvider_Fragment  extends Fragment {


    String TAG = "ContentProvider_frag";
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.contentprovider_fragment, container, false);

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
        String[] columns = new String[]{
                mySQLiteHelper.KEY_NAME,
                mySQLiteHelper.KEY_SCORE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.name,
                R.id.score
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                requireContext(), R.layout.highscore,
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

                // Should really create a dialogfragment and display all the contact info here.  but I'll get to that
                // when I have time.
                //String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String name = cursor.getString(cursor.getColumnIndex(mySQLiteHelper.KEY_NAME));
                Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show();

            }
        });

        return myView;
    }

}
