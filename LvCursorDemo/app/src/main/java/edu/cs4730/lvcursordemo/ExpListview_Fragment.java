package edu.cs4730.lvcursordemo;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;


public class ExpListview_Fragment extends Fragment{
	String TAG = "explistview_frag";
	Context myContext;

	CursorTreeAdapter myCursorAdapter;
	ExpandableListView expListView;
	CntDbAdapter dbHelper;
	SimpleCursorAdapter dataAdapter;

	public ExpListview_Fragment() {
		// Required empty public constructor
	}


	// a necessary class declared here so they can use the above variables
	// I'm sure there is a better way to do this, but this is fast and dirty.
	//likely could have passed the dbHelper in the constructor, then I would not have to subclass it.

	/*
	 * extend the simplecursortreeadapter, we must provide a constructor and getChildrenCursor method
	 */
	public class MySimpleCursorTreeAdapter extends SimpleCursorTreeAdapter {


		// Note that the constructor does not take a Cursor. This is done to avoid querying the 
		// database on the main thread.
		public MySimpleCursorTreeAdapter(Context context,
				Cursor groupCursor,
				int groupLayout,
				String[] groupFrom, 
				int[] groupTo, 
				int childLayout,
				String[] childrenFrom,
				int[] childrenTo) {

			super(context, groupCursor, groupLayout, groupFrom, groupTo, childLayout, childrenFrom,  childrenTo);
		}


		@Override
		protected Cursor getChildrenCursor(Cursor groupCursor) {
			// Given the group, we return a cursor for all the children within that group 

			//so get the Continent out of the cursor and then query for these items and go with it.
			String inputText = groupCursor.getString(1);  //should be second column (ie 1) I think...
			Log.v("gCC","child continent is " + inputText);
			Cursor mCursor = dbHelper.fetchChild(inputText);
			return mCursor;
		}

	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.activity_explistview, container, false);


		//first get the group cursor.
		dbHelper = new CntDbAdapter(myContext);
		dbHelper.open();
		Cursor gcursor = dbHelper.fetchGroup();

		//get the listview
		expListView = (ExpandableListView) myView.findViewById(R.id.lvExp);

		myCursorAdapter = new MySimpleCursorTreeAdapter(
				myContext,
				gcursor,
				R.layout.evl_group_row,  //header/group/parent layout
				new String[] { CntDbAdapter.KEY_CONTINENT }, // Name of the columns in DB.
				new int[] {R.id.evl_row_name },  //name of views in layout.

				R.layout.evl_child_row,  //child layout
				new String[] { CntDbAdapter.KEY_CODE,    //name of the columns in DB in order
						CntDbAdapter.KEY_NAME, CntDbAdapter.KEY_REGION }, 
						new int[] { R.id.evl_code,  R.id.evl_name, R.id.evl_region}  //name of the layoud ids.
				);

		expListView.setAdapter(myCursorAdapter);

		return myView;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = activity.getApplicationContext();
		Log.d(TAG,"onAttach");
	}		
}
