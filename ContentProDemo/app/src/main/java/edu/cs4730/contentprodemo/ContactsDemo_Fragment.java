package edu.cs4730.contentprodemo;

import android.Manifest;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.annotation.NonNull;

import edu.cs4730.contentprodemo.databinding.ContactsFragBinding;

/**
 * A simple fragment to display the contacts lists
 */
public class ContactsDemo_Fragment extends Fragment {

    String TAG = "Contacts_frag";
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;
    ContactsFragBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ContactsFragBinding.inflate(inflater, container, false);
        setupContactsList();

        return binding.getRoot();
    }

    /**
     * this method is used to setup the view and make sure it has permissions as well.
     */
    public void setupContactsList() {
        //first check to see if I have permissions (marshmallow) if I don't then ask, otherwise start up the demo.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //I'm on not explaining why, just asking for permission.
            Log.v(TAG, "asking for permissions");
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, MainActivity.REQUEST_READ_CONTACTS);

        } else {
            //get the people URI
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            //setup the information we want for the contentprovider.
            String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};

            //just for fun, sort return data by name, which instead of default which is _ID I think.
            String SortOrder = ContactsContract.Contacts.DISPLAY_NAME;  //"column name, column name"  except only have one column name.

            //finally make the query
            // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //depreicated method, use one below.
            cursor = requireActivity().getContentResolver().query(CONTENT_URI, projection, null, null, SortOrder);

            //this is commented out, because better way is to use a listview, which is what the rest of the code does.
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

            binding.listView1.setClickable(true);

            // The desired columns to be bound
            String[] columns = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};

            // the XML defined views which the data will be bound to
            int[] to = new int[]{R.id.name, R.id.phone};

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
            dataAdapter = new SimpleCursorAdapter(requireActivity(), R.layout.contactlist, cursor, columns, to, 0);


            // Assign adapter to ListView
            binding.listView1.setAdapter(dataAdapter);
            //set click listener
            binding.listView1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) binding.listView1.getItemAtPosition(position);

                    //quickly display the name.
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
