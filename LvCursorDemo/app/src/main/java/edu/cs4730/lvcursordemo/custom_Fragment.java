package edu.cs4730.lvcursordemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class custom_Fragment extends Fragment implements Button.OnClickListener {

    String TAG = "custom_frag";
    Context myContext;

    private CntDbAdapter dbHelper;
    private CustomCursorAdapter dataAdapter;
    Button add;

    public custom_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.custom_fragment, container, false);


        dbHelper = new CntDbAdapter(myContext);
        dbHelper.open();

        //Clean all data
        dbHelper.deleteAllCountries();
        //Add some data
        dbHelper.insertSomeCountries();

        //Generate ListView from SQLite Database
        dataAdapter = new CustomCursorAdapter(myContext, dbHelper.fetchAllCountries());

        ListView listView = (ListView) myView.findViewById(R.id.listView1C);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(myContext,
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

        EditText myFilter = (EditText) myView.findViewById(R.id.myFilterC);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });

        //add button, which will add Canada on to the list and disable itself.
        add = (Button) myView.findViewById(R.id.addC);
        add.setOnClickListener(this);

        return myView;

    }

    @Override
    public void onClick(View v) {

        dbHelper.createCountry("CND", "Canda", "North America", "North America");

        dataAdapter.changeCursor(dbHelper.fetchAllCountries());
        add.setEnabled(false);  //since not changing the CODE, any more adds will fail in the database.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        Log.d(TAG, "onAttach");
    }
}
