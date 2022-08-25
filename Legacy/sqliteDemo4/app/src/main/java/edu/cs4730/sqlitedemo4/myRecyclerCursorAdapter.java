package edu.cs4730.sqlitedemo4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Seker on 2/20/2015.
 * This extends CursorRecyclerAdatper Created skywin and found at https://gist.github.com/Shywim/127f207e7248fe48400b
 * which is included in my java code.
 */
public class myRecyclerCursorAdapter extends CursorRecyclerAdapter<myRecyclerCursorAdapter.ViewHolder> {

    public myRecyclerCursorAdapter(Context context, Cursor cursor) {
        // super(context,cursor);
        super(cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mScore;

        public ViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.name);
            mScore = (TextView) view.findViewById(R.id.score);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.recycler_row, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    @SuppressLint("Range")
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor) {
        //finally set the information in the text fields.
        String name = cursor.getString(cursor.getColumnIndex(MainActivity.KEY_NAME));
//        int score = cursor.getInt(cursor.getColumnIndex(MainActivity.KEY_SCORE));
        String scoreStr = cursor.getString(cursor.getColumnIndex(MainActivity.KEY_SCORE));

        viewHolder.mName.setText(name);

        viewHolder.mScore.setText(scoreStr);
        //viewHolder.mTextView.setText(myListItem.getName());
    }
}
