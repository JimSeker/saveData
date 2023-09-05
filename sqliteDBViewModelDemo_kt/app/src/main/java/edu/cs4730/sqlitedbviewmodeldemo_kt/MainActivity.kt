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
import edu.cs4730.sqlitedbviewmodeldemo_kt.databinding.ActivityMainBinding
import edu.cs4730.sqlitedbviewmodeldemo_kt.db.CursorViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mAdapter: myAdapter
    lateinit var mViewModel: CursorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this).get(CursorViewModel::class.java)

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.itemAnimator = DefaultItemAnimator()
        mAdapter = myAdapter(null, applicationContext)
        //add the adapter to the recyclerview
        binding.list.adapter = mAdapter
        mViewModel.data.observe(this,
            Observer<Cursor?> { data -> mAdapter.setCursor(data) })

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            mViewModel.add("Jim", 3012)
            mViewModel.add("Danny", 312)
        })
    }
}