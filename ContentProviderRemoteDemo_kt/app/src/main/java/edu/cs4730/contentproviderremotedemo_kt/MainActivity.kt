package edu.cs4730.contentproviderremotedemo_kt

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs4730.contentproviderremotedemo_kt.databinding.ActivityMainBinding

public class MainActivity : AppCompatActivity() {


    companion object {

        //database columns

        @JvmStatic val KEY_NAME: String = "Name"
        @JvmStatic val KEY_SCORE: String = "Score"
        @JvmStatic val KEY_ROWID: String = "_id" //required field for the cursorAdapter

        // for the ContentProSQliteDBDemo use this one.
        @JvmStatic
        public var CONTENT_URI: Uri = Uri.parse("content://edu.cs4730.scoreprovider_kt/score")
        // for the ContentProviderroomDemo  use this one.
        // @JvmStatic var CONTENT_URI: Uri = Uri.parse("content://edu.cs4730.scoreroomprovider/score");
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var mViewModel: CursorViewModel
    private lateinit var mAdapter: myAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        //initialize the View Model
        mViewModel = ViewModelProvider(this)[CursorViewModel::class.java]

        //setup the RecyclerView
        binding.list.setLayoutManager(LinearLayoutManager(this))
        binding.list.setItemAnimator(DefaultItemAnimator())

        mAdapter = myAdapter(this, mViewModel) //null, since ModelView and LiveData will handle it.
        //add the adapter to the recyclerview
        binding.list.setAdapter(mAdapter)

        //This button is used to add more data, so the loader will then reload "on it's own".

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            val initialValues = ContentValues()
            initialValues.put(MainActivity.KEY_NAME, "Jim")
            initialValues.put(MainActivity.KEY_SCORE, "3012")
            val uri = contentResolver.insert(CONTENT_URI, initialValues)
        })
    }
}