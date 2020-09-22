package edu.cs4730.sqlitedemo4;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class CursorViewModel extends AndroidViewModel {

    private static final String TAG = "CursorViewModel";
    ContentProviderLiveData myLiveData;

    public CursorViewModel(@NonNull Application application) {
        super(application);

        //for sqldemo use this one
        myLiveData = new ContentProviderLiveData(application, RecyclerFragment.CONTENT_URI);
        //contenProviderRoomDemo use this one
        //myLiveData = new ContentProviderLiveData(application, RecyclerFragment.CONTENT_URI2);
    }

    public ContentProviderLiveData getData() {
        return myLiveData;
    }

    public Cursor getCursor() {
        return myLiveData.getValue();
    }
}
