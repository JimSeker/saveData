package edu.cs4730.sqlitedbdemo_kt

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import edu.cs4730.sqlitedbdemo_kt.databinding.RecyclerRowBinding
import edu.cs4730.sqlitedbdemo_kt.db.mySQLiteHelper

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */
class myAdapter     //constructor
    (private var mCursor: Cursor?, private val mContext: Context) :
    RecyclerView.Adapter<myAdapter.ViewHolder>() {
    // the viewbinding provides the references now.
    inner class ViewHolder(var viewBinding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {}

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = RecyclerRowBinding.inflate(LayoutInflater.from(mContext), viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        mCursor!!.moveToPosition(i)
        viewHolder.viewBinding.name.text =
            mCursor!!.getString(mCursor!!.getColumnIndex(mySQLiteHelper.KEY_NAME))
        viewHolder.viewBinding.score.text =
            mCursor!!.getInt(mCursor!!.getColumnIndex(mySQLiteHelper.KEY_SCORE)).toString()
        //itemView is the whole cardview, so it easy to a click listener.
        viewHolder.itemView.setOnClickListener {
            Toast.makeText(mContext, viewHolder.viewBinding.name.getText(), Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (mCursor == null) 0 else mCursor!!.count
    }

    //change the cursor as needed and have the system redraw the data.
    @SuppressLint("NotifyDataSetChanged")
    fun setCursor(c: Cursor?) {
        mCursor = c
        notifyDataSetChanged()
    }
}