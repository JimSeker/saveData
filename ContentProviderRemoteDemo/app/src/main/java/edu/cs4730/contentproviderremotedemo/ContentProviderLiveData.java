package edu.cs4730.contentproviderremotedemo;

import android.app.Application;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * This is used to set liveData pieces, we need to know when the contentprovider cursor has changed.
 *
 */

public class ContentProviderLiveData extends MutableLiveData<Cursor> {

    private final String TAG = "ContentProviderLiveData";
    private Context mContext;
    private ContentObserver mObserver;
    private Uri mUri;

    ContentProviderLiveData(@NonNull Application application, Uri uri) {
        super();
        mContext = application.getApplicationContext();
        mUri = uri;
        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.d(TAG, "ContentObserver.onChange()");
                postValue(getContentProviderValue());
            }
        };
    }

    //either just started, or came out hibernation, load everything and set the listener.
    @Override
    protected void onActive() {
        Log.d(TAG, "onActive()");
        //nothing loaded at first, we need to kick start it.
        setValue(getContentProviderValue());
        //set the observer to listener and now we are ready.
        mContext.getContentResolver().registerContentObserver(mUri, true, mObserver);

    }

    //when the LiveData is not active, remove the listener
    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive()");
        mContext.getContentResolver().unregisterContentObserver(mObserver);
    }

    /**
     * fill this in so you can actually get the data from a content provider.
     * To make things simple, The query is in a method, since it has to be called from two places.
     */
    private Cursor getContentProviderValue() {
        String[] projection = new String[]{MainActivity.KEY_ROWID, MainActivity.KEY_NAME, MainActivity.KEY_SCORE};
        String SortOrder = MainActivity.KEY_SCORE;

        return mContext.getContentResolver().query(mUri, projection, null, null, SortOrder);

    }

}
