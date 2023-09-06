package edu.cs4730.lvcursordemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.cs4730.lvcursordemo.databinding.CustomFragmentBinding;
import edu.cs4730.lvcursordemo.db.countryDatabase;

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
    CustomFragmentBinding binding;
    private countryDatabase countryDB;
    private CustomCursorAdapter dataAdapter;

    public custom_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = CustomFragmentBinding.inflate(inflater, container, false);


        countryDB = new countryDatabase(requireContext());
        countryDB.open();

        //Clean all data
        countryDB.deleteAllCountries();
        //Add some data
        countryDB.insertSomeCountries();

        //Generate ListView from SQLite Database
        Log.d(TAG, "creating the adapter");
        dataAdapter = new CustomCursorAdapter(requireContext(), countryDB.fetchAllCountries());

        Log.d(TAG, "getting the listview");
        // Assign adapter to ListView
        binding.listView1C.setAdapter(dataAdapter);


        binding.listView1C.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(requireContext(), countryCode, Toast.LENGTH_SHORT).show();
            }
        });

        binding.myFilterC.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return countryDB.fetchCountriesByName(constraint.toString());
            }
        });

        //add button, which will add Canada on to the list and disable itself.
        binding.addC.setOnClickListener(this);

        return binding.getRoot();

    }

    @Override
    public void onClick(View v) {

        countryDB.insertCountry("CND", "Canda", "North America", "North America");

        dataAdapter.changeCursor(countryDB.fetchAllCountries());
        binding.addC.setEnabled(false);  //since not changing the CODE, any more adds will fail in the database.
    }

}
