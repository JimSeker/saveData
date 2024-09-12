package edu.cs4730.contentproviderremotedemo_kt

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.cs4730.contentproviderremotedemo_kt.MainActivity.Companion.KEY_NAME
import edu.cs4730.contentproviderremotedemo_kt.MainActivity.Companion.KEY_ROWID
import edu.cs4730.contentproviderremotedemo_kt.MainActivity.Companion.KEY_SCORE
import edu.cs4730.contentproviderremotedemo_kt.databinding.RecyclerRowBinding

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */
@SuppressLint("NotifyDataSetChanged")
class myAdapter(lifecycleOwner: LifecycleOwner, private val mViewModel: CursorViewModel) :
    RecyclerView.Adapter<myAdapter.ViewHolder>() {
    private var cursor: Cursor? = null

    private val TAG = "myAdapter"

    //viewbinding provides the references now.
    class ViewHolder(var viewBinding: RecyclerRowBinding) : RecyclerView.ViewHolder(
        viewBinding.root
    )

    //constructor
    init {
        //the observer could just as easily be in the MainActivity and the use a "swapCursor" method
        // to change the cursor and call notifyDataSetChanged()
        mViewModel.data.observe(lifecycleOwner) { data ->
            cursor = data
            notifyDataSetChanged()
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = RecyclerRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (setRow(i)) {  //move the cursor to the correct spot in the list other no data.
            val name = cursor!!.getString(cursor!!.getColumnIndex(KEY_NAME))
            val scoreStr = cursor!!.getString(cursor!!.getColumnIndex(KEY_SCORE))

            //note the key_score is int value, but we can request it as a string.
            viewHolder.viewBinding.name.text = name
            viewHolder.viewBinding.score.text = scoreStr
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    /**
     * @see android.widget.ListAdapter.getItemId
     */
    @SuppressLint("Range")
    override fun getItemId(position: Int): Long {
        return if (cursor != null) {
            if (cursor!!.moveToPosition(position)) {
                cursor!!.getLong(cursor!!.getColumnIndex(KEY_ROWID))
            } else {
                0
            }
        } else {
            0
        }
    }

    /**
     * my class to find the correct row in the cursor
     */
    private fun setRow(position: Int): Boolean {
        if (cursor != null) {
            return cursor!!.moveToPosition(position)
        }
        return false //no data!
    }
}
