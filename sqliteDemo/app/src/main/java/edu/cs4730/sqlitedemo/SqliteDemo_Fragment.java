package edu.cs4730.sqlitedemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.cs4730.sqlitedemo.db.ScoreDatabase;
import edu.cs4730.sqlitedemo.db.mySQLiteHelper;

/**
 * A demo of sqlite
 */
public class SqliteDemo_Fragment extends Fragment {

    String TAG = "sqlitedemo_frag";
    Context myContext;
    Button runAgain;

    TextView output;
    ScoreDatabase db;

    public SqliteDemo_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.sqlitedemo_fragment, container, false);
        output = (TextView) myView.findViewById(R.id.textView1);
        db = new ScoreDatabase(myContext);
        db.open();  //if database doesn't exist, it has now created.
        democode();
        runAgain = (Button) myView.findViewById(R.id.again);
        runAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                democode();
            }
        });
        return myView;
    }

    //a simple method to append information to the Textview
    public void appendthis(String item) {
        output.append(item + "\n");
    }


    //on resume, open the database again.
    @Override
    public void onResume() {
        super.onResume();
        if (db == null)
            db = new ScoreDatabase(myContext);
        if (!db.isOpen())
            db.open();
    }

    //onclose, we are in the background, so just close the DB.
    @Override
    public void onPause() {
        super.onPause();
        if (db.isOpen())
            db.close();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        Log.d(TAG, "onAttach");
    }

    /*
    *  This code will insert data into a empty DB and return.
    *  if there is data, then it will display information about the data in the table.
     */
    public void democode() {
        appendthis("Start of Demo code.");

        Cursor c;
        //check to see if there is any data
        c = db.getAllNames();

        if (c == null) {
            //nothing in the database ?  maybe the database query failed.
            appendthis("No DB found, creating and inserting data");
            //insert data.
            db.insertName("Jim", 3012);
            db.insertName("Brandon", 312);
            return;
        }
        //check to see if no data?
        if (c.getCount() == 0) {
            //no data return
            appendthis("empty DB, inserting data");
            db.insertName("Jim", 3012);
            db.insertName("Brandon", 312);
            return;
        }
        appendthis("There is already data, so just displaying it.");
        //display any data.
        //moveToFirst() should not be needed, but just in case.
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            //Column: 0 ROWID, 1 name, 2 score
            //We could just use c.getString(1) because we know column 1 is name, but it
            //not very readable and if we were change the DB, then this could would not work
            //instead using a longer, but more readable and DB change method.
            //so not using this appendthis(	c.getString(1) + " " + c.getInt(2));
            appendthis(c.getString(c.getColumnIndex(mySQLiteHelper.KEY_NAME)) + " " +
                    c.getInt(c.getColumnIndex(mySQLiteHelper.KEY_SCORE)));
        }
        c.close();  //release the resources, before I use it again.
        //test on return 1 item.

        c = db.get1name("Jim");  //test of query for 1.
        //A note, get1name only returns two columns, name and score.  while getall returns three
        //columns.

        //c = db.get1nameR("Jim");   //test of rawQuery
        if (c.getCount() == 0) {
            appendthis("failed on select 1 item.");
        } else {
            appendthis("Select on Jim returned: " +
                    c.getString(c.getColumnIndex(mySQLiteHelper.KEY_NAME)) + " " +  //column 0
                    c.getInt(c.getColumnIndex(mySQLiteHelper.KEY_SCORE))            //column 1
            );
        }
        c.close(); //release the resources
        c = null;
    }


}
