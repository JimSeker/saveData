package edu.cs4730.sqlitedbdemo_kt

import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.View
import edu.cs4730.sqlitedbdemo_kt.db.mySQLiteHelper
import android.widget.Toast

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */
class myAdapter     //constructor
    (private var mCursor: Cursor?, private val rowLayout: Int, private val mContext: Context) :
    RecyclerView.Adapter<myAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var myName: TextView
        var myScore: TextView

        init {
            myName = itemView.findViewById(R.id.name)
            myScore = itemView.findViewById(R.id.score)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(rowLayout, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        mCursor!!.moveToPosition(i)
        viewHolder.myName.text =
            mCursor!!.getString(mCursor!!.getColumnIndex(mySQLiteHelper.KEY_NAME))
        viewHolder.myScore.text =
            mCursor!!.getInt(mCursor!!.getColumnIndex(mySQLiteHelper.KEY_SCORE)).toString()
        //itemView is the whole cardview, so it easy to a click listener.
        viewHolder.itemView.setOnClickListener { v ->
            val tv =
                v.findViewById<TextView>(R.id.name) //view in this case is the itemView, which had other pieces in it.
            Toast.makeText(mContext, tv.text, Toast.LENGTH_SHORT).show()
        }
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (mCursor == null) 0 else mCursor!!.count
    }

    //change the cursor as needed and have the system redraw the data.
    fun setCursor(c: Cursor?) {
        mCursor = c
        notifyDataSetChanged()
    }
}