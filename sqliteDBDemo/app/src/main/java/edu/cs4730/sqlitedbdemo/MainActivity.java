package edu.cs4730.sqlitedbdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.cs4730.sqlitedbdemo.db.ScoreDatabase;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This is a "simple" sqlite database example.   It uses a recyclerview and a simple ish adapter
 * to display the data.  There is a much better adapter that understands cursors in SqliteDemo3 and 4.
 */

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FloatingActionButton fab;
    myAdapter mAdapter;

    ScoreDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the database and open it.
        db = new ScoreDatabase(getApplicationContext());
        db.open();  //if database doesn't exist, it has now created.




        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myAdapter(db.getAllNames(), R.layout.recycler_row, getApplicationContext());
        //add the adapter to the recyclerview
        mRecyclerView.setAdapter(mAdapter);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insertName("Jim", 3012);
                db.insertName("Danny", 312);
                mAdapter.setCursor(db.getAllNames());
            }
        });

        //with with the Itemlisteners we could easy add delete, click listener is there already, but
        //a modelview would be useful to implement an update feature.

    }
}
