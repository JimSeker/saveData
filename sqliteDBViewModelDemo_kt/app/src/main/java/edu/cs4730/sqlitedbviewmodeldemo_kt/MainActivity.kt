package edu.cs4730.sqlitedbviewmodeldemo_kt

import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        mViewModel = ViewModelProvider(this).get(CursorViewModel::class.java)

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.itemAnimator = DefaultItemAnimator()
        mAdapter = myAdapter(null, applicationContext)
        //add the adapter to the recyclerview
        binding.list.adapter = mAdapter
        mViewModel.data.observe(this, Observer<Cursor?> { data -> mAdapter.setCursor(data) })

        binding.floatingActionButton.setOnClickListener {
            mViewModel.add("Jim", 3012)
            mViewModel.add("Danny", 312)
        }
    }
}