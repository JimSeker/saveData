package edu.cs4730.lvcursordemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import edu.cs4730.lvcursordemo.databinding.ActivityExplistviewBinding;
import edu.cs4730.lvcursordemo.db.DatabaseHelper;
import edu.cs4730.lvcursordemo.db.countryDatabase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.SimpleCursorTreeAdapter;


public class ExpListview_Fragment extends Fragment {
    String TAG = "explistview_frag";
    ActivityExplistviewBinding binding;

    CursorTreeAdapter myCursorAdapter;
    countryDatabase countryDB;
    SimpleCursorAdapter dataAdapter;

    public ExpListview_Fragment() {
        // Required empty public constructor
    }

    /**
     * extend the simplecursortreeadapter, we must provide a constructor and getChildrenCursor method
     */
    public class MySimpleCursorTreeAdapter extends SimpleCursorTreeAdapter {

        // Note that the constructor does not take a Cursor. This is done to avoid querying the
        // database on the main thread.
        public MySimpleCursorTreeAdapter(Context context, Cursor groupCursor, int groupLayout, String[] groupFrom, int[] groupTo, int childLayout, String[] childrenFrom, int[] childrenTo) {

            super(context, groupCursor, groupLayout, groupFrom, groupTo, childLayout, childrenFrom, childrenTo);
        }


        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            // Given the group, we return a cursor for all the children within that group

            //so get the Continent out of the cursor and then query for these items and go with it.
            String inputText = groupCursor.getString(1);  //should be second column (ie 1) I think...
            Log.v("gCC", "child continent is " + inputText);
            return countryDB.fetchChild(inputText);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ActivityExplistviewBinding.inflate(inflater, container, false);


        //first get the group cursor.
        countryDB = new countryDatabase(requireContext());
        countryDB.open();
        Cursor gcursor = countryDB.fetchGroup();

        //get the listview
        myCursorAdapter = new MySimpleCursorTreeAdapter(requireContext(), gcursor, R.layout.evl_group_row,  //header/group/parent layout
                new String[]{DatabaseHelper.KEY_CONTINENT}, // Name of the columns in DB.
                new int[]{R.id.evl_row_name},  //name of views in layout.

                R.layout.evl_child_row,  //child layout
                new String[]{DatabaseHelper.KEY_CODE,    //name of the columns in DB in order
                        DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_REGION}, new int[]{R.id.evl_code, R.id.evl_name, R.id.evl_region}  //name of the layoud ids.
        );

        binding.lvExp.setAdapter(myCursorAdapter);

        return binding.getRoot();
    }
}
