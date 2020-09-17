package edu.cs4730.lvcursordemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.io.IOException;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;


public class countryDatabase {
    private static final String TAG = "CountriesDbAdapter";
    private SupportSQLiteOpenHelper  helper;
    private SupportSQLiteDatabase db;

    //constructor
    public countryDatabase(Context ctx) {
        SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(ctx)
            .name(DatabaseHelper.DATABASE_NAME)
            .callback(new DatabaseHelper())
            .build();
        helper = factory.create(configuration);

    }

    //---opens the database---
    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    //returns true if db is open.  Helper method.
    public boolean isOpen() throws SQLException {
        return db.isOpen();
    }

    //---closes the database---
    public void close() {
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long insertCountry(String code, String name, String continent, String region) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_CODE, code);
        initialValues.put(DatabaseHelper.KEY_NAME, name);
        initialValues.put(DatabaseHelper.KEY_CONTINENT, continent);
        initialValues.put(DatabaseHelper.KEY_REGION, region);

        return db.insert(DatabaseHelper.SQLITE_TABLE, CONFLICT_FAIL, initialValues);
    }

    public boolean deleteAllCountries() {

        int doneDelete = 0;
        doneDelete = db.delete(DatabaseHelper.SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    //this one uses the supportQueryBuilder that build a SupportSQLiteQuery for the query.
    public Cursor qbQuery(String TableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SupportSQLiteQueryBuilder qb = SupportSQLiteQueryBuilder.builder(TableName);
        qb.columns(projection);
        qb.selection(selection, selectionArgs);
        qb.orderBy(sortOrder);
        //using the query builder to manage the actual query at this point.
        return db.query(qb.create());
    }


    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = qbQuery(DatabaseHelper.SQLITE_TABLE,
                 new String[] {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_CODE, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_CONTINENT, DatabaseHelper.KEY_REGION},
                null, null, null);

        }
        else {
            mCursor = qbQuery(DatabaseHelper.SQLITE_TABLE,
                new String[] {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_CODE, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_CONTINENT, DatabaseHelper.KEY_REGION},
                DatabaseHelper.KEY_NAME + " like '%" + inputText + "%'",
                null,null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllCountries() {

        Cursor mCursor =  qbQuery(DatabaseHelper.SQLITE_TABLE,
            new String[] {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_CODE, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_CONTINENT, DatabaseHelper.KEY_REGION},
            null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public void insertSomeCountries() {

        insertCountry("AFG","Afghanistan","Asia","Southern and Central Asia");
        insertCountry("ALB","Albania","Europe","Southern Europe");
        insertCountry("DZA","Algeria","Africa","Northern Africa");
        insertCountry("ASM","American Samoa","Oceania","Polynesia");
        insertCountry("AND","Andorra","Europe","Southern Europe");
        insertCountry("AGO","Angola","Africa","Central Africa");
        insertCountry("AIA","Anguilla","North America","Caribbean");

    }

    /*
     * These two are for the Expandable List View demo piece.
     */
    public Cursor fetchGroup() {
        //Return ID and continent information, but all of it.
        Cursor mCursor =  qbQuery(DatabaseHelper.SQLITE_TABLE,
            new String[] {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_CONTINENT},
            null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchChild(String inputText) {
        //fetching all the children for a group header, based on continent.

        Cursor mCursor =  qbQuery(DatabaseHelper.SQLITE_TABLE,
            new String[] {DatabaseHelper.KEY_ROWID, DatabaseHelper.KEY_CODE, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_REGION},
            DatabaseHelper.KEY_CONTINENT + " = '" + inputText + "'",
            null,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


}
