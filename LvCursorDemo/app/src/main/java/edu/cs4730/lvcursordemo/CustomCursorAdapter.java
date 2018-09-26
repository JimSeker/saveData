package edu.cs4730.lvcursordemo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;

public class CustomCursorAdapter extends CursorAdapter implements OnClickListener {

    String TAG = "CustomAdapter";
    Context myContext;
    private LayoutInflater inflater;

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);  //0 no content observer on the cursor.  use CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER for

        //needed for the toast later in the code.
        myContext = context;

        //set the inflater for the newView
        inflater = LayoutInflater.from(context);
    }

    /**
     * setup the view from our layout, no data is set here.  Just create the view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.country_custom_info, parent, false);
    }

    /**
     * BindView, is where we set the data fro the view.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        TextView textViewcode = view.findViewById(R.id.codeC);
        //textViewcode.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        textViewcode.setText(cursor.getString(cursor.getColumnIndex(CntDbAdapter.KEY_CODE)));

        TextView textViewname = view.findViewById(R.id.nameC);
        textViewname.setText(cursor.getString(cursor.getColumnIndex(CntDbAdapter.KEY_NAME)));

        TextView textViewcont = view.findViewById(R.id.continentC);
        textViewcont.setText(cursor.getString(cursor.getColumnIndex(CntDbAdapter.KEY_CONTINENT)));

        TextView textViewregion = view.findViewById(R.id.regionC);
        textViewregion.setText(cursor.getString(cursor.getColumnIndex(CntDbAdapter.KEY_REGION)));

        Button btn = view.findViewById(R.id.delC);
        btn.setTag(cursor.getString(cursor.getColumnIndex(CntDbAdapter.KEY_ROWID)));
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String row_id = (String) view.getTag();
        Toast.makeText(myContext, row_id, Toast.LENGTH_SHORT).show();

        //onContentChanged();  //if the Database changes, notify the adapter.
        //changeCursor (Cursor cursor)  //Change the underlying cursor to a new cursor. If there is an existing cursor it will be closed.
    }
}
