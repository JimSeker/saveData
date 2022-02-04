package edu.cs4730.sqlitedbviewmodeldemo.db;

import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * basic class to create/update the database we are using.
 * This is used in our database class. The table name and column
 * names are defined here as Constants, so they can be used else where easily.
 */

public class mySQLiteHelper extends SupportSQLiteOpenHelper.Callback {

    private static final String TAG = "mySQLiteHelper";

    //column names for the table.
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    public static final String DATABASE_NAME = "myScore.db";
    public static final String TABLE_NAME = "HighScore";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
        "CREATE TABLE " + TABLE_NAME + " (" +
            KEY_ROWID + " integer PRIMARY KEY autoincrement," +  //this line is required for the cursorAdapter.
            KEY_NAME + " TEXT, " +
            KEY_SCORE + " INTEGER );";

    //required constructor with the super.
    public mySQLiteHelper() {
        super(DATABASE_VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        //NOTE only called when the database is initial created!
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        //Called when the database version changes, Remember the constant from above.
        Log.w(TAG, "Upgrading database from version " + oldVersion
            + " to "
            + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
