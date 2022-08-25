package edu.cs4730.contentprosqlitedbdemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import edu.cs4730.contentprosqlitedbdemo.db.mySQLiteHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a simple fragment to demo how a spinner works with a cursor.
 * Note, you don't have use a spinner separately, this is just so to separate the code
 * for ease of reading.
 */
public class SpinnerFragment extends Fragment {

    String TAG = "spinner_frag";
    Spinner mySpinner;
    private SimpleCursorAdapter dataAdapter;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_spinner, container, false);

        //get the people URI
        Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
        //setup the information we want for the contentprovider.
        String[] projection = new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE};

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        String SortOrder = mySQLiteHelper.KEY_SCORE;  //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);

        mySpinner = myView.findViewById(R.id.spinner1);

        dataAdapter = new SimpleCursorAdapter(requireContext(),
            android.R.layout.simple_spinner_item,
            cursor,  //data  _id and column name at min.
            new String[]{mySQLiteHelper.KEY_NAME},  //column name to display
            new int[]{android.R.id.text1}, 0);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(dataAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {  //the viewpager is causing the fragment to remove the view, but not removing the listener.  which causes a force close, because the view is null
                    String name = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                    Toast.makeText(requireContext(), "Selected ID=" + id + "name is " + name, Toast.LENGTH_LONG).show();
                } else {
                    Log.wtf(TAG, "View is null in listener!");
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return myView;
    }
}