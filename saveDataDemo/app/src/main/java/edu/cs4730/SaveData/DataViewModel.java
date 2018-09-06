package edu.cs4730.SaveData;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private MutableLiveData<Integer> d4;

    public LiveData<Integer> getData() {
        if (d4 == null) {
            d4 = new MutableLiveData<Integer>();
            d4.setValue(0);
        }
        return d4;
    }

    public int getValue() {
        if (d4 != null)
            return d4.getValue().intValue();
        else
            return 0;
    }

    public void increment() {
        if (d4 != null)
            d4.setValue(d4.getValue() + 1 );
        else {
            d4 = new MutableLiveData<Integer>();
            d4.setValue(1);
        }
    }
}
