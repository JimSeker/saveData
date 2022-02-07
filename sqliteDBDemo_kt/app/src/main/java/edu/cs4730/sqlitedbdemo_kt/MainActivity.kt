package edu.cs4730.sqlitedbdemo_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.cs4730.sqlitedbdemo_kt.db.ScoreDatabase

/**
 * This is a "simple" sqlite database example.   It uses a recyclerview and a simple ish adapter
 * to display the data.  There is a much better adapter that understands cursors in SqliteDemo3 and 4.
 */

class MainActivity : AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var mAdapter: myAdapter

    lateinit var db: ScoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get the database and open it.
        db = ScoreDatabase(applicationContext)
        db.open() //if database doesn't exist, it has now created.
        mRecyclerView = findViewById(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mAdapter = myAdapter(db.allNames, R.layout.recycler_row, applicationContext)
        //add the adapter to the recyclerview
        mRecyclerView.adapter = mAdapter
        fab = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener(View.OnClickListener {
            db.insertName("Jim", 3012)
            db.insertName("Danny", 312)
            mAdapter.setCursor(db.allNames)
        })
    }
}