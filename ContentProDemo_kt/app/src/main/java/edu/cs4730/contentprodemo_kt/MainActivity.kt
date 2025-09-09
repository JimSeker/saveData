package edu.cs4730.contentprodemo_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cs4730.contentprodemo_kt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var TAG: String = "MainActivity"

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
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