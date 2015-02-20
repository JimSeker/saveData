package edu.cs4730.sqlitedemo3;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Seker on 2/20/2015.
 *
 */
public class myRecyclerCursorAdapter extends CursorRecyclerAdapter<myRecyclerCursorAdapter.ViewHolder> {

    public myRecyclerCursorAdapter(Context context,Cursor cursor){
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
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor) {
        //finally set the information in the text fields.
        String name= cursor.getString(cursor.getColumnIndex(MainActivity.KEY_NAME));
//        int score = cursor.getInt(cursor.getColumnIndex(MainActivity.KEY_SCORE));
        String scoreStr = cursor.getString(cursor.getColumnIndex(MainActivity.KEY_SCORE));

          viewHolder.mName.setText(name);

          viewHolder.mScore.setText(scoreStr);
        //viewHolder.mTextView.setText(myListItem.getName());
    }
}
