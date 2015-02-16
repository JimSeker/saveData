package edu.cs4730.sqlitedemo;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A demo of sqlite
 * 
 */
public class SqliteDemo_Fragment extends Fragment {

	String TAG = "sqlitedemo_frag";
	Context myContext;


	TextView output;
	ScoreDatabase db;

	public SqliteDemo_Fragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView =  inflater.inflate(R.layout.sqlitedemo_fragment, container, false);
		output = (TextView) myView.findViewById(R.id.textView1);
		db = new ScoreDatabase(myContext);
		db.open();  //if database doesn't exist, it has now created.
		democode();
		db.close();
		return myView;
	}
	   
    @Override
	public void onResume() {
      super.onResume(); 
     if (db == null) 
        db = new ScoreDatabase(myContext);
     if (!db.isOpen())
       db.open();
    }
    @Override
	public void onPause() {
      super.onPause();
      if (db.isOpen())
        db.close();
 
    }
    
    
    public void democode() {
    	appendthis("Creating or Using a SQLite database");
    	
    	Cursor c;
    	//check to see if there is any data
    	c = db.getAllNames();

    	if (c == null) {
    		//nothing in the database ?  maybe the database query failed.
    		appendthis("Insert data in empty db");
    		//insert data.
    		db.insertName("Jim", 3012);
    		db.insertName("Brandon", 312);
    		return;
    	}
    	//check to see if no data?
    	if (c.getCount() == 0) {
    		//no data return
    		appendthis("Insert data in empty db");
    		db.insertName("Jim", 3012);
    		db.insertName("Brandon", 312);
    		return;
    	}
    	//display any data.
    	//moveToFirst() should not be needed, but just in case.
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            //0 name, 1 score
        	appendthis(	c.getString(1) + " " + c.getInt(2)
            		);  //note, ignoring the first column, which is the ROWID.
        }
        c.close();  //release the resources, before I use it again.
        //test on return 1 item.
        
        c = db.get1name("Jim");  //test of query for 1.
        //c = db.get1nameR("Jim");   //test of rawQuery
        if (c.getCount() == 0) {
        	appendthis("failed on select 1 item.");
        } else {
        	appendthis("Select on Jim returned: " +
            		c.getString(0) + " " + c.getInt(1) + "\n"
            		);
        }
        c.close(); //release the resources
        c = null;
    }
	public void appendthis(String item) {
		output.append("\n" + item);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = activity.getApplicationContext();
		Log.d(TAG,"onAttach");
	}

}
