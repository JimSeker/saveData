package edu.cs4730.datastoredemo_kt

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import edu.cs4730.datastoredemo_kt.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * This is an example of the newer and more complex DataStore methods to replace the sharepreferences
 * Honestly, until they make you, stay with sharedpreferences, simple and easier to use.  this is neither
 *
 * The kotlin and java version do the same thing, but do not match.  The kotlin version uses things that
 * not available to java, so instead we use the rxjava3 to get the flowable stuff to work correctly,
 * seriously changes the way the code looks.  but still provide the same functionality.
 *
 * the kotlin version is simpler to understand.
 */

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    var nothing: Int = 0
    private var myDataStore: Int = 0

    companion object {  //kotlin equivalent of static in java.  otherwise, error multiple dataStores active.
        private val DS = intPreferencesKey("DS")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "settings"
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            nothing++
            binding.tvNothing.text = nothing.toString()
            myDataStore++
            binding.tvDatastore.text = myDataStore.toString()
        }

    }

    //actually the method to store the data.  needs a coroutine to use it in onPause.
    private suspend fun saveDataStore() {
        dataStore.edit { preferences ->
            preferences[DS] = myDataStore
        }
    }

    //method to load the data, but it's still a flow, use coroutines in onResume to get it.
    fun loadDataStore(): Flow<Int> =
        dataStore.data.map { preferences ->
            preferences[DS]?.toInt() ?: 0
        }


    override fun onPause() {
        super.onPause()
        logthis("OnPause Called")
        //store the data in an async method.
        CoroutineScope(Dispatchers.IO).launch { saveDataStore() }
    }

    override fun onResume() {
        super.onResume()
        logthis("OnResume called")
        //use the coroutines to collect the data from an async call.
        CoroutineScope(Dispatchers.IO).launch {
            loadDataStore().collect { myInt ->
                myDataStore = myInt
                binding.tvDatastore.text = myDataStore.toString()
            }
        }

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