package edu.cs4730.contentprodemo.data;

import android.database.Cursor;
import android.database.MatrixCursor;

/**
 * A very simple data class that return a  cursors with the cube of the values.
 */
public class Cube {
    String[]  Column;

    public Cube() {
        Column = new String[] { "number", "cube"};
    }

    //getone returns the val and the square of the val in cursor.
    public Cursor getone(int val) {
        //must return a Cursor, MatrixCursor is an editable cursor.
        MatrixCursor myCursor = new MatrixCursor(Column);
        myCursor.addRow(new Object[] { val, val*val*val });
        return myCursor;
    }

    //returns the number and number of the number from 1 to 10.
    public Cursor getall() {
        MatrixCursor myCursor = new MatrixCursor(Column);
        for (int i=1; i<11; i++) {
            myCursor.addRow(new Object[] { i, i*i*i});
        }
        return myCursor;
    }
}
