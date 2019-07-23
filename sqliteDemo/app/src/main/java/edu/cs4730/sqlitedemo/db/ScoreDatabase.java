package edu.cs4730.sqlitedemo.db;

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

/*
 * This an accessor class that do all the work
 * in the database.  This is the object that the
 * system uses to access/insert/update/etc the database.
 *
 * This provides a number of methods as examples for different things.
 * It also provides the accessor methods for the content provider as well.
 *
 *
 */

public class ScoreDatabase {


    private SupportSQLiteOpenHelper helper;  //mySQLiteHelper DBHelper;
    private SupportSQLiteDatabase db;

    //constructor
    public ScoreDatabase(Context ctx) {
        SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(ctx)
            .name(mySQLiteHelper.DATABASE_NAME)
            .callback(new mySQLiteHelper())
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

    /*
     *  The following are insert methods
     */
    //----insert an entry -----
    public long insertName(String name, Integer value) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(mySQLiteHelper.KEY_NAME, name);
        initialValues.put(mySQLiteHelper.KEY_SCORE, value);
        return db.insert(mySQLiteHelper.TABLE_NAME, CONFLICT_FAIL, initialValues);
    }

    public long cpInsert(String TableName, ContentValues values) {
        return db.insert(TableName, CONFLICT_FAIL, values);
    }
    /*
     *  The following a different query examples.
     */

    //---get all the rows.
    public Cursor getAllNames() {
        //SELECT KEY_NAME, KEY_SCORE FROM DATABASE_TABLE SORTBY KEY_NAME;
        /*
        We can do it this way or use the helper function created for the for the ContentProvider, below.

        Cursor c = db.query(mySQLiteHelper.TABLE_NAME,
                new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE},
                null, null, null, null,
                mySQLiteHelper.KEY_NAME);  //sort by name.
         */

        Cursor mCursor = cpQuery(mySQLiteHelper.TABLE_NAME,   //table name
            new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE},  //projection, ie columns.
            null,  //selection,  we want everything.
            null, // String[] selectionArgs,  again, we want everything.
            mySQLiteHelper.KEY_NAME// String sortOrder  by name as the sort.
        );
        if (mCursor != null)  //make sure db is not empty!
            mCursor.moveToFirst();
        return mCursor;
    }

    //Retrieve one entry  METHOD we are supposed to use.
    public Cursor get1name(String name) throws SQLException {
        /*
        //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs,
        //String groupBy, String having, String orderBy, String limit)
        //distinct 	true if you want each row to be unique, false otherwise.
        //table 	The table name to compile the query against.
        //columns 	A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
        //selection 	A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
        //selectionArgs 	You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
        //groupBy 	A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
        //having 	A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
        //orderBy 	How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
        //limit 	Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no LIMIT clause.
        Cursor mCursor =
                db.query(true, mySQLiteHelper.TABLE_NAME,
                        new String[]{mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE,
                        },
                        mySQLiteHelper.KEY_NAME + "=\'" + name + "\'",
                        null,
                        null,
                        null,
                        null,
                        null);
                        */
        //We are going to use the helper function the content provider instead of a query.
        Cursor mCursor = cpQuery(mySQLiteHelper.TABLE_NAME,   //table name
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
        //public Cursor rawQuery (String sql, String[] selectionArgs)
        //sql 	the SQL query. The SQL string must not be ; terminated
        //selectionArgs 	You may include ?s in where clause in the query, which will be replaced by the values from selectionArgs. The values will be bound as Strings.
        Cursor mCursor =
            db.query("select Name, Score from HighScore where Name=\'" + name + "\'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //this one is used as a wrapper for the ContentProvider.
    public Cursor cpQuery(String TableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SupportSQLiteQueryBuilder qb = SupportSQLiteQueryBuilder.builder(TableName);
        qb.columns(projection);
        qb.selection(selection, selectionArgs);
        qb.orderBy(sortOrder);
        //using the query builder to manage the actual query at this point.
        return db.query(qb.create());
    }

    /*
     *  The following are update methods examples.
     */

    // ---updates a row---
    public boolean updateRow(String name, int score) {
        ContentValues args = new ContentValues();
        args.put(mySQLiteHelper.KEY_SCORE, score);
        //returns true if one or more updates happened, otherwise false.
        //return db.update(mySQLiteHelper.TABLE_NAME, args, mySQLiteHelper.KEY_NAME + "= \'" + name + "\'", null) > 0;
        //better method is the use the generic method below.
        return cpUpdate(mySQLiteHelper.TABLE_NAME, args, mySQLiteHelper.KEY_NAME + "= \'" + name + "\'", null) > 0;
    }

    // this is a generic method to update something from the database.   The contentProvider calls this method.
    public int cpUpdate(String TableName, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(TableName, CONFLICT_FAIL, values, selection, selectionArgs);
    }

    /*
     *  the following are delete methods
     */
    // this is a generic method to delete something from the database.   The contentProvider calls this method.
    public int cpDelete(String TableName, String selection, String[] selectionArgs) {
        return db.delete(TableName, selection, selectionArgs);
    }

    //remove all entries from the CurrentBoard
    public void emptyName() {
        db.delete(mySQLiteHelper.TABLE_NAME, null, null);
    }

}
