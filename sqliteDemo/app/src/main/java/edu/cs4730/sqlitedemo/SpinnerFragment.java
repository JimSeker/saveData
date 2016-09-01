package edu.cs4730.sqlitedemo;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
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
 * A simple {@link Fragment} subclass.
 */
public class SpinnerFragment extends Fragment {
    String TAG = "spinner_frag";
    Context myContext;
    Spinner mySpinner;
    private SimpleCursorAdapter dataAdapter;
    ScoreDatabase db;

    public SpinnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.spinner_fragment, container, false);
        mySpinner = (Spinner) myView.findViewById(R.id.spinner1);
        db = new ScoreDatabase(myContext);
        db.open();  //if database doesn't exist, it has now created.
        Cursor myCursor = db.getAllNames();
        dataAdapter = new SimpleCursorAdapter(myContext,
                android.R.layout.simple_spinner_item,
                myCursor,  //data  _id and column name at min.
                new String[]{mySQLiteHelper.KEY_NAME},  //column name to display
                new int[]{android.R.id.text1}, 0);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(dataAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                Toast.makeText(myContext, "Selected ID=" + id + "name is " + name, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        Log.d(TAG, "onAttach");
    }


}
