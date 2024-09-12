package edu.cs4730.contentprosqlitedbdemo_kt

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import edu.cs4730.contentprosqlitedbdemo_kt.databinding.FragmentContentProviderAccessBinding
import edu.cs4730.contentprosqlitedbdemo_kt.db.mySQLiteHelper

/**
 * Very simple version, that accesses, displays, and add some data to the database via the contentProvider
 */
class ContentProviderAccessFragment : Fragment() {
    var TAG: String = "ContentProvider_frag"
    var cursor: Cursor? = null
    lateinit var binding: FragmentContentProviderAccessBinding
    private lateinit var dataAdapter: SimpleCursorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContentProviderAccessBinding.inflate(inflater, container, false)

        //get the people URI, defined in myDBContentProvider, so don't do it here again.
        // Uri CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score");
        //setup the information we want for the contentprovider.
        val projection =
            arrayOf(mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE)

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        val SortOrder =
            mySQLiteHelper.KEY_SCORE //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        cursor = requireActivity().contentResolver.query(
            myDBContentProvider.CONTENT_URI,
            projection,
            null,
            null,
            SortOrder
        )

        if (cursor == null) {
            Log.i(TAG, "cursor is null...")
        }

        // The desired columns to be bound
        val columns = arrayOf(mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE)

        // the XML defined views which the data will be bound to
        val to = intArrayOf(R.id.name, R.id.score)

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter =
            SimpleCursorAdapter(requireContext(), R.layout.highscore, cursor, columns, to, 0)

        binding.list.adapter = dataAdapter
        binding.list.onItemClickListener =
            OnItemClickListener { listView, view, position, id -> // Get the cursor, positioned to the corresponding row in the result set
                val cursor = listView.getItemAtPosition(position) as Cursor

                // Should really create a dialogbox and display all the contact info here.  but I'll get to that
                // when I have time.
                //String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") val name =
                    cursor.getString(cursor.getColumnIndex(mySQLiteHelper.KEY_NAME))
                Toast.makeText(requireContext(), name, Toast.LENGTH_SHORT).show()
            }

        binding.floatingActionButton.setOnClickListener {
            val initialValues = ContentValues()
            initialValues.put(mySQLiteHelper.KEY_NAME, "Fred")
            initialValues.put(mySQLiteHelper.KEY_SCORE, "123")
            val uri = requireActivity().contentResolver.insert(
                myDBContentProvider.CONTENT_URI,
                initialValues
            )

            //a view model fixes this and won't be needed.
            cursor = requireActivity().contentResolver.query(
                myDBContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                SortOrder
            )
            dataAdapter.swapCursor(cursor)
        }

        return binding.root
    }

    //a simple method to append information to the Textview
    fun logthis(item: String?) {
        Log.d(TAG, item!!)
    }
}