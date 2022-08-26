package edu.cs4730.contentproviderremotedemo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private Cursor cursor;
    private int rowLayout;
    private final String TAG = "myAdapter";
    private CursorViewModel mViewModel;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mScore;

        private final String TAG = "ViewHolder";

        public ViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mScore = view.findViewById(R.id.score);
        }
    }

    //constructor
    public myAdapter(LifecycleOwner lifecycleOwner, int rowLayout, CursorViewModel mv) {
        this.rowLayout = rowLayout;
        mViewModel = mv;
        //the observer could just as easily be in the MainActivity and the use a "swapCursor" method
        // to change the cursor and call notifyDataSetChanged()
        mViewModel.getData().observe(lifecycleOwner, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor data) {
                cursor =data;
                notifyDataSetChanged();
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    @SuppressLint("Range")
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (setRow(i)) {  //move the cursor to the correct spot in the list other no data.
            String name = cursor.getString(cursor.getColumnIndex(MainActivity.KEY_NAME));
            String scoreStr = cursor.getString(cursor.getColumnIndex(MainActivity.KEY_SCORE));
              //note the key_score is int value, but we can request it as a string.

            viewHolder.mName.setText(name);
            viewHolder.mScore.setText(scoreStr);

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * @see android.widget.ListAdapter#getItemId(int)
     */
    @Override
    @SuppressLint("Range")
    public long getItemId(int position) {
        if ( cursor != null) {
            if (cursor.moveToPosition(position)) {
                return cursor.getLong(cursor.getColumnIndex(MainActivity.KEY_ROWID));
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * my class to find the correct row in the cursor
     */
    private boolean setRow(int position) {
        if (cursor != null) {
            return cursor.moveToPosition(position);
        }
        return false; //no data!
    }
}
