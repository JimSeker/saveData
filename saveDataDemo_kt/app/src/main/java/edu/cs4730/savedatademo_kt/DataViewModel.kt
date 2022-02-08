package edu.cs4730.savedatademo_kt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val d4: MutableLiveData<Int>
    val data: LiveData<Int>
        get() = d4
    val value: Int
        get() = d4.value!!.toInt()

    fun increment() {
        d4.value = d4.value!! + 1
    }

    init {
        d4 = MutableLiveData()
        d4.value = 0
    }
}