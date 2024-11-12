package edu.cs4730.contentprosqlitedbdemo_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import edu.cs4730.contentprosqlitedbdemo_kt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        val KEY_NAME: String = "Name"
    }

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        binding.navView.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item -> //setup the fragments here.
                val id = item.itemId
                if (id == R.id.action_first) {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.container.id, ContentProviderAccessFragment())
                        .commit()
                    item.setChecked(true)
                    return@OnItemSelectedListener true
                } else if (id == R.id.action_second) {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.container.id, RecyclerViewFragment()).commit()
                    item.setChecked(true)
                } else if (id == R.id.action_third) {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.container.id, SpinnerFragment()).commit()
                    item.setChecked(true)
                }
                false
            }

        )
        //start it with the first fragment, if we just started the app.
        if (savedInstanceState == null) supportFragmentManager.beginTransaction()
            .replace(binding.container.id, ContentProviderAccessFragment()).commit()
    }

}