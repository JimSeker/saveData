package edu.cs4730.sqlitedbviewmodeldemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.io.IOException;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;

/**
 * This an accessor class that do all the work in the database.  This is the object that the
 * system uses to access/insert/update/etc the database.
 * <p>
 * This provides a number of methods as examples for different things.
 * It also provides the accessor methods for the content provider as well.
 */

public class ScoreDatabase {


    private final SupportSQLiteOpenHelper helper;
    private SupportSQLiteDatabase db;

    //constructor
    public ScoreDatabase(Context ctx) {
       // SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(ctx)
            .name(mySQLiteHelper.DATABASE_NAME)
            .callback(new mySQLiteHelper())
            .build();
        helper = new FrameworkSQLiteOpenHelperFactory().create(configuration);

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

    /**
     * insert methods.
     */
    //InsertName is wrapper method, so the activity doesn't have to build  ContentValues for the insert.
    public long insertName(String name, Integer value) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(mySQLiteHelper.KEY_NAME, name);
        initialValues.put(mySQLiteHelper.KEY_SCORE, value);
        return db.insert(mySQLiteHelper.TABLE_NAME, CONFLICT_FAIL, initialValues);
    }


    //This is the method to actually do an insert using the convenience method.  Note, it uses fail when there is a conflict.
    public long Insert(String TableName, ContentValues values) {
        return db.insert(TableName, CONFLICT_FAIL, values);
    }

    /**
     * The following a different ways to query the database.  They all return a Cursor.
     */

    //get all the rows.
    public Cursor getAllNames() {
        //SELECT KEY_NAME, KEY_SCORE FROM DATABASE_TABLE SORTBY KEY_NAME;
        Cursor mCursor = qbQuery(mySQLiteHelper.TABLE_NAME,   //table name
            new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE},  //projection, ie columns.
            null,  //selection,  we want everything.
            null, // String[] selectionArgs,  again, we want everything.
            mySQLiteHelper.KEY_NAME// String sortOrder  by name as the sort.
        );
        if (mCursor != null)  //make sure cursor is not empty!
            mCursor.moveToFirst();
        return mCursor;
    }

    //Retrieve one entry  METHOD we are supposed to use.
    public Cursor get1name(String name) throws SQLException {
        //the query parameter method is not included in supportSQLiteDatabase.
        //So, use the helper function
        Cursor mCursor = qbQuery(mySQLiteHelper.TABLE_NAME,   //table name
            new String[]{mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE},  //projection, ie columns.
            mySQLiteHelper.KEY_NAME + "=\'" + name + "\'",  //selection,
            null, // String[] selectionArgs,  not necessary here.
            mySQLiteHelper.KEY_NAME// String sortOrder  by name as the sort.
        );

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //retrieve one entry, using RAW method  (ie sql statement)
    public Cursor get1nameR(String name) {
        //public Cursor query (String sql, Object[] bindArgs)
        //sql 	the SQL query. The SQL string must not be ; terminated
        //BindArgs 	You may include ?s in where clause in the query, which will be replaced by the values from selectionArgs. The values will be bound as Strings.
        Cursor mCursor =
            db.query("select Name, Score from HighScore where Name=\'" + name + "\'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
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

    /**
     * The following are update methods examples.
     */

    // This is a wrapper method, so that main code doesn't have ContentValues.
    public boolean updateRow(String name, int score) {
        ContentValues args = new ContentValues();
        args.put(mySQLiteHelper.KEY_SCORE, score);
        //returns true if one or more updates happened, otherwise false.
        return Update(mySQLiteHelper.TABLE_NAME, args, mySQLiteHelper.KEY_NAME + "= \'" + name + "\'", null) > 0;
    }

    // this is a generic method to update something from the database, uses the Convenience method.
    public int Update(String TableName, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(TableName, CONFLICT_FAIL, values, selection, selectionArgs);
    }

    /**
     * the following are delete methods
     */
    // this uses the Convenience method to delete something from the database.
    public int Delete(String TableName, String selection, String[] selectionArgs) {
        return db.delete(TableName, selection, selectionArgs);
    }

    //remove all entries from the CurrentBoard
    public void emptydb() {
        db.delete(mySQLiteHelper.TABLE_NAME, null, null);
    }
}

