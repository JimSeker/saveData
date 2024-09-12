package edu.cs4730.contentprodemo_kt.data

import android.database.Cursor
import android.database.MatrixCursor

/**
 * * A very simple data class that return a cursors with the square of the values.
 */
class Square {
    private var Column: Array<String> = arrayOf("number", "square")

    //getone returns the val and the square of the val in cursor.
    fun getone(value: Int): Cursor {
        //must return a Cursor, MatrixCursor is an editable cursor.
        val myCursor = MatrixCursor(Column)
        myCursor.addRow(arrayOf<Any>(value, value * value))
        return myCursor
    }

    //returns the number and number of the number from 1 to 10.
    fun getall(): Cursor {
        val myCursor = MatrixCursor(Column)
        for (i in 1..10) {
            myCursor.addRow(arrayOf<Any>(i, i * i))
        }
        return myCursor
    }
}
