package edu.cs4730.filesystemdemo_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import edu.cs4730.filesystemdemo_kt.databinding.ActivityMainBinding

/**
 * Example code to demo how to read/write file to local private directory (localPrivate_Fragment),
 * local public (localPublic_Fragment) directory
 */

class MainActivity : AppCompatActivity() {
    var TAG: String = "MainActivity"
    lateinit var one: localPrivate_Fragment
    lateinit var two: localPublic_Fragment
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        one = localPrivate_Fragment()
        two = localPublic_Fragment()

        binding.bnv.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item -> //At this point, we are doing the same thing that is done for menu selections.
            //if we had a onOptionsItemSelect method for a menu, we could just use it.
            val id = item.itemId
            if (id == R.id.one) {
                supportFragmentManager.beginTransaction().replace(binding.container.id, one).commit()
                return@OnItemSelectedListener true
            } else if (id == R.id.two) {
                supportFragmentManager.beginTransaction().replace(binding.container.id, two).commit()
                return@OnItemSelectedListener true
            }
            false
        })

        if (savedInstanceState == null) {
            //set the first one as the default.
            supportFragmentManager.beginTransaction().add(binding.container.id, one).commit()
        }
    }
}