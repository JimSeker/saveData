package edu.cs4730.contentprodemo_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.cs4730.contentprodemo_kt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var TAG: String = "MainActivity"

    lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.action_CP) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, Contentp_Fragment()).commit()
                true
            } else if (item.itemId == R.id.action_contact) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, ContactsDemo_Fragment()).commit()
                true
            } else false
        }
        //set the first one as the default.
        supportFragmentManager.beginTransaction()
            .add(binding.container.id, Contentp_Fragment()).commit()
    }

}