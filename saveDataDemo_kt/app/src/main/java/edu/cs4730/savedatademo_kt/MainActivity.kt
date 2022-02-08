package edu.cs4730.savedatademo_kt

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

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

    lateinit var t1: EditText
    lateinit var logger: TextView
    lateinit var tv_nothing: TextView
    lateinit var tv_bundle: TextView
    lateinit var tv_preference: TextView
    lateinit var tv_viewmodel: TextView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logger = findViewById(R.id.log)
        t1 = findViewById(R.id.editText1)
        tv_nothing = findViewById(R.id.tv_nothing)
        tv_nothing.text = b1.toString()

        //bundle method
        tv_bundle = findViewById(R.id.tv_bundle)
        if (savedInstanceState != null) { //There is saved data
            logthis("There is data, restoring")
            b2 = savedInstanceState.getInt("b2", 0) //default in case of issues.
            tv_bundle.text = b2.toString()
        } else {
            logthis("No data in savedInstanceState")
        }

        //preference method
        tv_preference = findViewById(R.id.tv_preference)
        //settext handled in getprefs();
        getprefs()

        //lastly the model view
        tv_viewmodel = findViewById(R.id.tv_viewmodel)
        //for the model view live variable.
        mViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        mViewModel.data.observe(this) { data ->
            logthis("Data changed, updating!")
            tv_viewmodel.text = data.toString()
        }
        button = findViewById(R.id.button)
        button.setOnClickListener {
            //increment each number.  over rotations and starts, these numbers will be different.
            b1++
            tv_nothing.text = b1.toString()
            b2++
            tv_bundle.text = b2.toString()
            b3++
            tv_preference.text = b3.toString()
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
        tv_preference.text = b3.toString()
    }

    /**
     * Called, when app is being destroyed, but maybe called after onStop as well.
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        //Store the state et_bundle/b2
        savedInstanceState.putInt("b2", b2)
        super.onSaveInstanceState(savedInstanceState)
    }

    /**
     * simple method to add the log TextView.
     */
    fun logthis(newinfo: String) {
        if (newinfo.compareTo("") != 0) {
            logger.append(newinfo + "\n")
            Log.d(TAG, newinfo)
        }
    }
}