package edu.cs4730.SaveData;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DataViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> d4;

    public DataViewModel(@NonNull Application application) {
        super(application);
        d4 = new MutableLiveData<Integer>();
        d4.setValue(0);
    }

    public LiveData<Integer> getData() {
        return d4;
    }

    public int getValue() {
        return d4.getValue().intValue();

    }

    public void increment() {
        d4.setValue(d4.getValue() + 1);
    }
}
