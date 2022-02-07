package edu.cs4730.sqlitedbviewmodeldemo_kt

import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.cs4730.sqlitedbviewmodeldemo_kt.db.CursorViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var mAdapter: myAdapter
    lateinit var mViewModel: CursorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(CursorViewModel::class.java)
        mRecyclerView = findViewById(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mAdapter = myAdapter(null, R.layout.recycler_row, applicationContext)
        //add the adapter to the recyclerview
        mRecyclerView.adapter = mAdapter
        mViewModel.data.observe(this,
            Observer<Cursor?> { data -> mAdapter.setCursor(data) })
        fab = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener(View.OnClickListener {
            mViewModel.add("Jim", 3012)
            mViewModel.add("Danny", 312)
        })
    }
}