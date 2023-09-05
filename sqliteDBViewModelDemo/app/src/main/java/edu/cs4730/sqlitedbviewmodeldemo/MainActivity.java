package edu.cs4730.sqlitedbviewmodeldemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.cs4730.sqlitedbviewmodeldemo.databinding.ActivityMainBinding;
import edu.cs4730.sqlitedbviewmodeldemo.db.CursorViewModel;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    myAdapter mAdapter;

    CursorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(CursorViewModel.class);

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myAdapter(null,getApplicationContext());
        //add the adapter to the recyclerview
        binding.list.setAdapter(mAdapter);

        mViewModel.getData().observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor data) {
                mAdapter.setCursor(data);
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.add("Jim", 3012);
                mViewModel.add("Danny", 312);
            }
        });
    }
}