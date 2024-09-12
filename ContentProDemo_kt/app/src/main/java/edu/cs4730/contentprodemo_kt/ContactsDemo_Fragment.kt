package edu.cs4730.contentprodemo_kt

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import edu.cs4730.contentprodemo_kt.databinding.ContactsFragBinding

/**
 * A simple fragment to display the contacts lists
 */
class ContactsDemo_Fragment : Fragment() {
    var TAG: String = "Contacts_frag"
    var cursor: Cursor? = null
    private var dataAdapter: SimpleCursorAdapter? = null
    lateinit var binding: ContactsFragBinding

    lateinit var rpl: ActivityResultLauncher<Array<String>>
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setup for the read permissions needed.
        rpl = registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            var granted = true
            for ((key, value) in isGranted) {
                logthis("$key is $value")
                if (!value) granted = false
            }
            if (granted) {
                logthis("All permissions granted")
                setupContactsList()
            } else {
                Toast.makeText(
                    requireContext(), "Contacts access NOT granted", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ContactsFragBinding.inflate(inflater, container, false)
        setupContactsList()

        return binding.root
    }

    /**
     * this method is used to setup the view and make sure it has permissions as well.
     */
    fun setupContactsList() {
        //first check to see if I have permissions (marshmallow) if I don't then ask, otherwise start up the demo.
        if (!allPermissionsGranted()) {
            rpl.launch(REQUIRED_PERMISSIONS)
        } else {
            //get the people URI
            val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI
            //setup the information we want for the contentprovider.
            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
            )

            //just for fun, sort return data by name, which instead of default which is _ID I think.
            val SortOrder =
                ContactsContract.Contacts.DISPLAY_NAME //"column name, column name"  except only have one column name.

            //finally make the query
            // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //depreicated method, use one below.
            cursor = requireActivity().contentResolver.query(
                CONTENT_URI, projection, null, null, SortOrder
            )

            //this is commented out, because better way is to use a listview, which is what the rest of the code does.
            //	  if (c.moveToFirst()) {
            //	 	do {
            //	 		String str = "Id: " + c.getString(0);
            //	 		str += "Name: " + c.getString(1);
            //	 	} while (c.moveToNext());
            //	 }

            //setup the listview for the fragment
            if (cursor == null) {
                logthis("cursor is null...")
            }
            logthis("setup up listview")

            binding.listView1.isClickable = true

            // The desired columns to be bound
            val columns = arrayOf(
                ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER
            )

            // the XML defined views which the data will be bound to
            val to = intArrayOf(R.id.name, R.id.phone)

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
            dataAdapter =
                SimpleCursorAdapter(requireActivity(), R.layout.contactlist, cursor, columns, to, 0)


            // Assign adapter to ListView
            binding.listView1.adapter = dataAdapter
            //set click listener
            binding.listView1.onItemClickListener =
                OnItemClickListener { parent, v, position, id -> // Get the cursor, positioned to the corresponding row in the result set
                    val cursor = binding.listView1.getItemAtPosition(position) as Cursor

                    //quickly display the name.
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
                }
        }
    }

    //ask for permissions when we start.
    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    //simple helper function to log information to two places.
    fun logthis(msg: String) {
        Log.d(TAG, msg)
    }
}
