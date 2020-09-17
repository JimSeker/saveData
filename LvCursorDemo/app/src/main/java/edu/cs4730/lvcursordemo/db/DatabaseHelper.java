package edu.cs4730.lvcursordemo.db;


import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class DatabaseHelper extends SupportSQLiteOpenHelper.Callback  {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_CONTINENT = "continent";
    public static final String KEY_REGION = "region";

    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "World";
    public static final String SQLITE_TABLE = "Country";
    public static final int DATABASE_VERSION = 1;


    private static final String DATABASE_CREATE =
        "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
            KEY_ROWID + " integer PRIMARY KEY autoincrement," +
            KEY_CODE + "," +
            KEY_NAME + "," +
            KEY_CONTINENT + "," +
            KEY_REGION + "," +
            " UNIQUE (" + KEY_CODE +"));";



    DatabaseHelper() {
        super(DATABASE_VERSION);
    }


    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        Log.w(TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}
