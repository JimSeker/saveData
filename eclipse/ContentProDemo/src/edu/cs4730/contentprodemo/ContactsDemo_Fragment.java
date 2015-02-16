package edu.cs4730.contentprodemo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



/**
 * A simple fragment to display the contacts lists
 * 
 * 
 */
public class ContactsDemo_Fragment extends Fragment {

	String TAG = "Contacts_frag";
	Context myContext;

	
	Cursor cursor;
	private SimpleCursorAdapter dataAdapter;
	ListView list;
	  
	public ContactsDemo_Fragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.contacts_frag, container, false);
		//get the people URI
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		//setup the information we want for the contentprovider.
		String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER };

		//just for fun, sort return data by name, which instead of default which is _ID I think.
		String SortOrder = ContactsContract.Contacts.DISPLAY_NAME;  //"column name, column name"  except only have one column name.

		//finally make the query
		// cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //depreicated method, use one below.
		cursor = getActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);

		//this is commented out, because better using a listview, which is what the rest of the code does.
		//	  if (c.moveToFirst()) {
		//	 	do {
		//	 		String str = "Id: " + c.getString(0);
		//	 		str += "Name: " + c.getString(1);
		//	 	} while (c.moveToNext());
		//	 }
		
		//setup the listview for the fragment 
		
  	  if (cursor == null) {
		  Log.e(TAG, "cursor is null...");
	  }
  	  Log.i(TAG, "setup up listview");
	  list = (ListView) myView.findViewById(R.id.listView1);
      list.setClickable(true);
	  
	  // The desired columns to be bound
	  String[] columns = new String[] {
			  ContactsContract.Contacts.DISPLAY_NAME,
			  ContactsContract.Contacts.HAS_PHONE_NUMBER
	  };
	 
	  // the XML defined views which the data will be bound to
	  int[] to = new int[] {
	    R.id.name,
	    R.id.phone
	  };
	 
	  // create the adapter using the cursor pointing to the desired data
	  //as well as the layout information
	  dataAdapter = new SimpleCursorAdapter(getActivity(), 
	    R.layout.contactlist,
	    cursor,
	    columns,
	    to,
	    0);
	 

	  // Assign adapter to ListView
	  list.setAdapter(dataAdapter);	
	  //set click listener
      list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				   // Get the cursor, positioned to the corresponding row in the result set
			     Cursor cursor = (Cursor) list.getItemAtPosition(position);
			    
			     // Should really create a dialogfragment and display all the contact info here. but I'll get to that
			     // when I have time.
			     String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			     Toast.makeText(myContext, name, Toast.LENGTH_SHORT).show();
			}
      });
	  return myView;
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = activity.getApplicationContext();
		Log.d(TAG,"onAttach");
	}

}
