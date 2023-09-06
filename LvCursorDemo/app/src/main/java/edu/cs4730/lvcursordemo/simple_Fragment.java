package edu.cs4730.lvcursordemo;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import edu.cs4730.lvcursordemo.databinding.SimpleFragmentBinding;
import edu.cs4730.lvcursordemo.db.DatabaseHelper;
import edu.cs4730.lvcursordemo.db.countryDatabase;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class simple_Fragment extends Fragment {
    String TAG = "simple_frag";
    SimpleFragmentBinding binding;
    private countryDatabase countryDB;
    private SimpleCursorAdapter dataAdapter;

    public simple_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = SimpleFragmentBinding.inflate(inflater, container, false);

        countryDB = new countryDatabase(requireContext());
        countryDB.open();

        //Clean all data
        countryDB.deleteAllCountries();
        //Add some data
        countryDB.insertSomeCountries();

        //Generate ListView from SQLite Database
        Cursor cursor = countryDB.fetchAllCountries();

        // The desired columns to be bound
        String[] columns = new String[]{DatabaseHelper.KEY_CODE, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_CONTINENT, DatabaseHelper.KEY_REGION};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.code, R.id.name, R.id.continent, R.id.region,};

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(  //Note SimpleCursorAdapter was added in API 11, so using the support.v4 version.
                requireContext(), R.layout.country_info, cursor, columns, to, 0);

        // Assign adapter to ListView
        binding.listView1.setAdapter(dataAdapter);

        binding.listView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(requireContext(), countryCode, Toast.LENGTH_SHORT).show();

            }
        });

        //create a listener for changes.
        binding.myFilter.addTextChangedListener(new TextWatcher() {

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

        return binding.getRoot();
    }
}
