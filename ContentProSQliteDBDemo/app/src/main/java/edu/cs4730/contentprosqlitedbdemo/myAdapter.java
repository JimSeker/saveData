package edu.cs4730.contentprosqlitedbdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.cs4730.contentprosqlitedbdemo.databinding.HighscoreBinding;
import edu.cs4730.contentprosqlitedbdemo.db.mySQLiteHelper;

/**
 * this adapter is very similar to the adapters used for listview, except a ViewHolder is required
 * see http://developer.android.com/training/improving-layouts/smooth-scrolling.html
 * except instead having to implement a ViewHolder, it is implemented within
 * the adapter.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    //viewbinding provides the references now.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public HighscoreBinding viewBinding;

        public ViewHolder(HighscoreBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }

    //constructor
    public myAdapter(Cursor c,  Context context) {
        this.mCursor = c;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        HighscoreBinding v = HighscoreBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        mCursor.moveToPosition(i);
        viewHolder.viewBinding.name.setText(
            mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_NAME))
        );
        viewHolder.viewBinding.score.setText(
            String.valueOf(mCursor.getInt(mCursor.getColumnIndex(mySQLiteHelper.KEY_SCORE)))
        );
        //itemView is the whole cardview, so it easy to a click listener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,  viewHolder.viewBinding.name.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    //change the cursor as needed and have the system redraw the data.
    @SuppressLint("NotifyDataSetChanged")
    public void setCursor(Cursor c) {
        mCursor = c;
        notifyDataSetChanged();
    }
}
