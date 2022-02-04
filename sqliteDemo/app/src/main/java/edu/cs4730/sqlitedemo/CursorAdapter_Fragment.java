package edu.cs4730.sqlitedemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.widget.TextView;
import android.widget.Toast;

import edu.cs4730.sqlitedemo.db.ScoreDatabase;
import edu.cs4730.sqlitedemo.db.mySQLiteHelper;

/**
 * This is a demo of how to use a cursor with a listview.  It uses a cursor adapter to do the work.
 * <p>
 * http://www.mysamplecode.com/2012/07/android-listview-cursoradapter-sqlite.html
 */

public class CursorAdapter_Fragment extends Fragment {
    String TAG = "cursorAdapter_frag";

    TextView output;
    ScoreDatabase db;
    private SimpleCursorAdapter dataAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.cursoradapter_fragment, container, false);
        output = myView.findViewById(R.id.textView1);

        db = new ScoreDatabase(requireContext());
        db.open();  //if database doesn't exist, it has now created.

        Cursor cursor = db.getAllNames();

        if (cursor == null) {
            Log.i("CAA", "cursor is null...");
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

        ListView listView = myView.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // display the name in a toast.
                String name = cursor.getString(cursor.getColumnIndexOrThrow(mySQLiteHelper.KEY_NAME));
                Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show();

            }
        });
        return myView;

    }


    @Override
    public void onResume() {
        super.onResume();
        if (db == null)
            db = new ScoreDatabase(requireContext());
        if (!db.isOpen())
            db.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (db.isOpen())
            db.close();

    }

}
