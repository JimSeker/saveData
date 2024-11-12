package edu.cs4730.sqlitedbdemo_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs4730.sqlitedbdemo_kt.databinding.ActivityMainBinding
import edu.cs4730.sqlitedbdemo_kt.db.ScoreDatabase

/**
 * This is a "simple" sqlite database example.   It uses a recyclerview and a simple ish adapter
 * to display the data.  There is a much better adapter that understands cursors in SqliteDemo3 and 4.
 */

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var mAdapter: myAdapter

    lateinit var db: ScoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        //get the database and open it.
        db = ScoreDatabase(applicationContext)
        db.open() //if database doesn't exist, it has now created.

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.itemAnimator = DefaultItemAnimator()
        mAdapter = myAdapter(db.allNames, applicationContext)
        //add the adapter to the recyclerview
        binding.list.adapter = mAdapter
        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            db.insertName("Jim", 3012)
            db.insertName("Danny", 312)
            mAdapter.setCursor(db.allNames)
        })
    }
}