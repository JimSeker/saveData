package edu.cs4730.lvcursordemo;

import android.annotation.SuppressLint;
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

import edu.cs4730.lvcursordemo.databinding.CountryCustomInfoBinding;
import edu.cs4730.lvcursordemo.databinding.CustomFragmentBinding;
import edu.cs4730.lvcursordemo.db.DatabaseHelper;

public class CustomCursorAdapter extends CursorAdapter implements OnClickListener {
    String TAG = "CustomAdapter";
    Context myContext;

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);  //0 no content observer on the cursor.  use CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER for
        //needed for the toast later in the code and layout inflater.
        myContext = context;
    }

    /**
     * setup the view from our layout, no data is set here.  Just create the view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //return inflater.inflate(R.layout.country_custom_info, parent, false);
        return CountryCustomInfoBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
    }

    /**
     * BindView, is where we set the data fro the view.
     */
    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data, view can't be null, so we change to viewbinding.
        CountryCustomInfoBinding binding = CountryCustomInfoBinding.bind(view);

        // that means, take the data from the cursor and put it in views
        //textViewcode.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        binding.codeC.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CODE)));

        binding.nameC.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NAME)));

        binding.continentC.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_CONTINENT)));

        binding.regionC.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_REGION)));

        binding.delC.setTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ROWID)));
        binding.delC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String row_id = (String) view.getTag();
        Toast.makeText(myContext, row_id, Toast.LENGTH_SHORT).show();

        //onContentChanged();  //if the Database changes, notify the adapter.
        //changeCursor (Cursor cursor)  //Change the underlying cursor to a new cursor. If there is an existing cursor it will be closed.
    }
}
