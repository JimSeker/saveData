package edu.cs4730.sqlitedbviewmodeldemo_kt.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.content.ContentValues
import android.database.Cursor

/**
 * a ViewModel that access the scoreDatabase.  This could actually replace the ScoreDatabase class,
 * but in this place, it just calls scoreDatabase class.
 */
class CursorViewModel(application: Application) : AndroidViewModel(application) {
    var db: ScoreDatabase
    var data = MutableLiveData<Cursor?>()

    // this uses the Convenience method to add something to the database and then update the cursor.
    fun add(name: String, value: Int) {
        db.open()
        db.insertName(name, value)
        data.value = db.allNames
        db.close()
    }

    // this uses the Convenience method to update something from the database and then update the cursor.
    fun Update(
        TableName: String, values: ContentValues, selection: String?,
        selectionArgs: Array<String?>?
    ): Int {
        db.open()
        val ret = db.Update(TableName, values, selection, selectionArgs)
        data.value = db.allNames
        db.close()
        return ret
    }

    // this uses the Convenience method to delete something in the database and then update the cursor.
    fun Delete(TableName: String, selection: String?, selectionArgs: Array<String?>?): Int {
        db.open()
        val ret = db.Delete(TableName, selection, selectionArgs)
        data.value = db.allNames
        db.close()
        return ret
    }

    init {
        db = ScoreDatabase(application)
        db.open()
        data.value = db.allNames
        db.close()
    }
}