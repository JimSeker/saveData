package edu.cs4730.sqlitedemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs4730.sqlitedemo.db.ScoreDatabase;
import edu.cs4730.sqlitedemo.db.mySQLiteHelper;

/**
 * This is a simple fragment to demo how a spinner works with a cursor.
 * Note, you don't have use a spinner separately, this is just so to separate the code
 * for ease of reading.
 */
public class SpinnerFragment extends Fragment {
    String TAG = "spinner_frag";
    Spinner mySpinner;
    private SimpleCursorAdapter dataAdapter;
    ScoreDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.spinner_fragment, container, false);
        mySpinner = (Spinner) myView.findViewById(R.id.spinner1);
        db = new ScoreDatabase(requireContext());
        db.open();  //if database doesn't exist, it has now been created.
        Cursor myCursor = db.getAllNames();
        dataAdapter = new SimpleCursorAdapter(requireContext(),
            android.R.layout.simple_spinner_item,
            myCursor,  //data  _id and column name at min.
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
