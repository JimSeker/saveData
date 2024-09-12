package edu.cs4730.contentproviderremotedemo_kt

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import edu.cs4730.contentproviderremotedemo_kt.MainActivity.Companion.CONTENT_URI

/**
 * This ViewModel then connects to the content provider and get the data itself via the ContentProviderLiveData class.
 */
class CursorViewModel(application: Application) : AndroidViewModel(application) {
    //get the data setup and ready to use.
    var data: ContentProviderLiveData = ContentProviderLiveData(application, CONTENT_URI)

    val cursor: Cursor?
        get() = data.value

    companion object {
        private const val TAG = "CursorViewModel"
    }
}
