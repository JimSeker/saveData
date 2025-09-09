package edu.cs4730.contentprosqlitedbdemo_kt

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import edu.cs4730.contentprosqlitedbdemo_kt.databinding.FragmentSpinnerBinding
import edu.cs4730.contentprosqlitedbdemo_kt.db.mySQLiteHelper

/**
 * This is a simple fragment to demo how a spinner works with a cursor.
 * Note, you don't have use a spinner separately, this is just so to separate the code
 * for ease of reading.
 */
class SpinnerFragment : Fragment() {
    var TAG: String = "spinner_frag"

    //Spinner mySpinner;
    private lateinit var dataAdapter: SimpleCursorAdapter
    var cursor: Cursor? = null
    lateinit var binding: FragmentSpinnerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment\
        binding = FragmentSpinnerBinding.inflate(inflater, container, false)


        //get the people URI
        //val CONTENT_URI = Uri.parse("content://edu.cs4730.scoreprovider/score")
        //setup the information we want for the contentprovider.
        val projection =
            arrayOf(mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE)

        //just for fun, sort return data by name, which instead of default which is _ID I think.
        val SortOrder =
            mySQLiteHelper.KEY_SCORE //"column name, column name"  except only have one column name.

        //finally make the query
        // cursor = managedQuery(CONTENT_URI, projection, null, null, null);  //deprecated method, use one below.
        cursor =
            requireActivity().contentResolver.query(
                myDBContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                SortOrder
            )

        dataAdapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            cursor,  //data  _id and column name at min.
            arrayOf(mySQLiteHelper.KEY_NAME),  //column name to display
            intArrayOf(android.R.id.text1), 0
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner1.adapter = dataAdapter

        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (view != null) {  //the viewpager is causing the fragment to remove the view, but not removing the listener.  which causes a force close, because the view is null
                    //no viewbinding from the view here, so findViewById is required from the adapter above.
                    val name =
                        (view.findViewById<View>(android.R.id.text1) as TextView).text.toString()
                    Toast.makeText(
                        requireContext(),
                        "Selected ID=" + id + "name is " + name,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.wtf(TAG, "View is null in listener!")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return binding.root
    }
}