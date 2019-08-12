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
        myLiveData = new ContentProviderLiveData(application, RecyclerFragment.CONTENT_URI);
    }

    public ContentProviderLiveData getData() {
        return myLiveData;
    }

    public Cursor getCursor() {
        return myLiveData.getValue();
    }
}
