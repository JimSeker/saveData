package edu.cs4730.contentproviderremotedemo_kt

import android.app.Application
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData

/**
 * This is used to set liveData pieces, we need to know when the contentprovider cursor has changed.
 *
 */
class ContentProviderLiveData internal constructor(
    application: Application,
    private val mUri: Uri
) : MutableLiveData<Cursor?>() {
    private val TAG = "ContentProviderLiveData"
    private val mContext: Context = application.applicationContext
    private val mObserver: ContentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            Log.d(TAG, "ContentObserver.onChange()")
            postValue(getContentProviderValue)
        }
    }

    //either just started, or came out hibernation, load everything and set the listener.
    override fun onActive() {
        Log.d(TAG, "onActive()")
        //nothing loaded at first, we need to kick start it.
        value = getContentProviderValue
        //set the observer to listener and now we are ready.
        mContext.contentResolver.registerContentObserver(mUri, true, mObserver)
    }

    //when the LiveData is not active, remove the listener
    override fun onInactive() {
        Log.d(TAG, "onInactive()")
        mContext.contentResolver.unregisterContentObserver(mObserver)
    }

    private val getContentProviderValue: Cursor?
        /**
         * fill this in so you can actually get the data from a content provider.
         * To make things simple, The query is in a method, since it has to be called from two places.
         */
        get() {
            val projection = arrayOf(MainActivity.KEY_ROWID, MainActivity.KEY_NAME, MainActivity.KEY_SCORE)
            val SortOrder = MainActivity.KEY_SCORE

            return mContext.contentResolver.query(mUri, projection, null, null, SortOrder)
        }
}
