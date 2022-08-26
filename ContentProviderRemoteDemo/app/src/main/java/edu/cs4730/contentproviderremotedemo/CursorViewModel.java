package edu.cs4730.contentproviderremotedemo;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * This ViewModel then connects to the content provider and get the data itself via the ContentProviderLiveData class.
 */

public class CursorViewModel extends AndroidViewModel {

    private static final String TAG = "CursorViewModel";
    ContentProviderLiveData myLiveData;

    public CursorViewModel(@NonNull Application application) {
        super(application);
        //get the data setup and ready to use.
        myLiveData = new ContentProviderLiveData(application, MainActivity.CONTENT_URI);

    }

    public ContentProviderLiveData getData() {
        return myLiveData;
    }

    public Cursor getCursor() {
        return myLiveData.getValue();
    }
}
