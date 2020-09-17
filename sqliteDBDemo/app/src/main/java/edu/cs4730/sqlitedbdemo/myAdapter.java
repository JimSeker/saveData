package edu.cs4730.sqlitedbdemo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cs4730.sqlitedbdemo.db.mySQLiteHelper;

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private Cursor mCursor;
    private int rowLayout;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView myName, myScore;


        public ViewHolder(View itemView) {
            super(itemView);
            myName = itemView.findViewById(R.id.name);
            myScore = itemView.findViewById(R.id.score);
        }
    }

    //constructor
    public myAdapter(Cursor c, int rowLayout, Context context) {
        this.mCursor = c;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        mCursor.moveToPosition(i);
        viewHolder.myName.setText(
            mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_NAME))
        );
        viewHolder.myScore.setText(
            String.valueOf(mCursor.getInt(mCursor.getColumnIndex(mySQLiteHelper.KEY_SCORE)))
        );
        //itemView is the whole cardview, so it easy to a click listener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = v.findViewById(R.id.name);  //view in this case is the itemView, which had other pieces in it.
                Toast.makeText(mContext, tv.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    //change the cursor as needed and have the system redraw the data.
    public void setCursor(Cursor c) {
        mCursor = c;
        notifyDataSetChanged();
    }
}
