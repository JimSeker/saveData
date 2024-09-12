package edu.cs4730.contentprosqlitedbdemo_kt

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs4730.contentprosqlitedbdemo_kt.databinding.FragmentRecyclerViewBinding
import edu.cs4730.contentprosqlitedbdemo_kt.db.mySQLiteHelper

/**
 * calling a content provider (local one) and using a recycler view.
 */
class RecyclerViewFragment : Fragment() {
    private lateinit var binding: FragmentRecyclerViewBinding
    private lateinit var mAdapter: myAdapter
    private var cursor: Cursor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)

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
            requireActivity().contentResolver.query(myDBContentProvider.CONTENT_URI, projection, null, null, SortOrder)

        binding.list2.layoutManager = LinearLayoutManager(requireContext())
        binding.list2.itemAnimator = DefaultItemAnimator()
        mAdapter = myAdapter(cursor, requireContext())
        //add the adapter to the recyclerview
        binding.list2.adapter = mAdapter

        binding.fab2.setOnClickListener {
            val initialValues = ContentValues()
            initialValues.put(mySQLiteHelper.KEY_NAME, "Danny")
            initialValues.put(mySQLiteHelper.KEY_SCORE, "1024")
            val uri = requireActivity().contentResolver.insert(myDBContentProvider.CONTENT_URI, initialValues)

            //a view model fixes this and won't be needed.
            cursor = requireActivity().contentResolver.query(
                myDBContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                SortOrder
            )
            mAdapter.setCursor(cursor)
        }

        //with with the Itemlisteners we could easy add delete, click listener is there already, but
        //a modelview would be useful to implement an update feature.
        return binding.root
    }
}