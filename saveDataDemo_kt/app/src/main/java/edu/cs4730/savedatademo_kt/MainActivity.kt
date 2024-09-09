package edu.cs4730.savedatademo_kt

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.cs4730.savedatademo_kt.databinding.ActivityMainBinding

/**
 * This example shows the difference with a viewmodel, sharedpeferences and instance bundle.
 * the shared preference system to store data, and viewmodel for when the app is rotated, and
 * for long term vs short term data storage.
 */

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    var b1 = 0
    var b2: Int = 0
    var b3: Int = 0
    lateinit var mViewModel: DataViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNothing.text = b1.toString()
        //bundle method
        if (savedInstanceState != null) { //There is saved data
            logthis("There is data, restoring")
            b2 = savedInstanceState.getInt("b2", 0) //default in case of issues.
            binding.tvBundle.text = b2.toString()
        } else {
            logthis("No data in savedInstanceState")
        }

        //settext handled in getprefs();
        getprefs()

        //lastly the model view
        //for the model view live variable.
        mViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        mViewModel.data.observe(this) { data ->
            logthis("Data changed, updating!")
            binding.tvViewmodel.text = data.toString()
        }
        binding.button.setOnClickListener {
            //increment each number.  over rotations and starts, these numbers will be different.
            b1++
            binding.tvNothing.text = b1.toString()
            b2++
            binding.tvBundle.text = b2.toString()
            b3++
            binding.tvPreference.text = b3.toString()
            mViewModel.increment() //settext is handled by the observer.
        }
    }

    override fun onPause() {
        super.onPause()
        logthis("OnPause called")
        // Store values between instances here
        val preferences = getSharedPreferences("example", MODE_PRIVATE)
        val editor = preferences.edit()

        //store b3 in preferences
        editor.putInt("b3", b3)
        editor.apply()
        logthis("Stored preferences")
    }

    override fun onResume() {
        super.onResume()
        logthis("OnResume called")
        getprefs()
    }

    /**
     * getpres() allows me to get the sharePreferences code in on place, it is called from
     * onCreate and onPause.
     */
    fun getprefs() {
        logthis("Restoring preferences.")
        // Get the between instance stored values
        val preferences = getSharedPreferences("example", MODE_PRIVATE)
        //get the key d3 and set a default value of "" if the key doesn't exist.  IE the first time this app is run.
        b3 = preferences.getInt("b3", 0)
        binding.tvPreference.text = b3.toString()
    }

    /**
     * Called, when app is being destroyed, but maybe called after onStop as well.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        //Store the state et_bundle/b2
        outState.putInt("b2", b2)
        super.onSaveInstanceState(outState)
    }

    /**
     * simple method to add the log TextView.
     */
    fun logthis(newinfo: String) {
        if (newinfo.compareTo("") != 0) {
            binding.log.append(newinfo + "\n")
            Log.d(TAG, newinfo)
        }
    }
}