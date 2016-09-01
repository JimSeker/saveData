package edu.cs4730.lvcursordemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class simple_Fragment extends Fragment {
	String TAG = "simple_frag";
	Context myContext;

	private CntDbAdapter dbHelper;
	private SimpleCursorAdapter dataAdapter;

	public simple_Fragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.simple_fragment, container, false);


		dbHelper = new CntDbAdapter(myContext);
		dbHelper.open();

		//Clean all data
		dbHelper.deleteAllCountries();
		//Add some data
		dbHelper.insertSomeCountries();

		//Generate ListView from SQLite Database
		Cursor cursor = dbHelper.fetchAllCountries();

		// The desired columns to be bound
		String[] columns = new String[] {
				CntDbAdapter.KEY_CODE,
				CntDbAdapter.KEY_NAME,
				CntDbAdapter.KEY_CONTINENT,
				CntDbAdapter.KEY_REGION
		};

		// the XML defined views which the data will be bound to
		int[] to = new int[] {
				R.id.code,
				R.id.name,
				R.id.continent,
				R.id.region,
		};

		// create the adapter using the cursor pointing to the desired data
		//as well as the layout information
		dataAdapter = new SimpleCursorAdapter(  //Note SimpleCursorAdapter was added in API 11, so using the support.v4 version.
				myContext, R.layout.country_info,
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

				// Get the state's capital from this row in the database.
				String countryCode =
						cursor.getString(cursor.getColumnIndexOrThrow("code"));
				Toast.makeText(myContext, countryCode, Toast.LENGTH_SHORT).show();

			}
		});

		EditText myFilter = (EditText) myView.findViewById(R.id.myFilter);
		//create a listener for changes.
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

		return myView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		myContext = context;
		Log.d(TAG,"onAttach");
	}
}
