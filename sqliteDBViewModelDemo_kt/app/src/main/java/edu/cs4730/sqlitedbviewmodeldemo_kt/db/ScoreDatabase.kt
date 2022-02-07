package edu.cs4730.sqlitedbdemo_kt.db

import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.Throws
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import edu.cs4730.sqlitedbdemo_kt.db.mySQLiteHelper
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import java.io.IOException

/**
 * This an accessor class that do all the work in the database.  This is the object that the
 * system uses to access/insert/update/etc the database.
 *
 *
 * This provides a number of methods as examples for different things.
 * It also provides the accessor methods for the content provider as well.
 */
class ScoreDatabase(ctx: Context?) {
    private val helper: SupportSQLiteOpenHelper
    private lateinit var db: SupportSQLiteDatabase

    //---opens the database---
    @Throws(SQLException::class)
    fun open() {
        db = helper.writableDatabase
    }

    //returns true if db is open.  Helper method.
    @get:Throws(SQLException::class)
    val isOpen: Boolean
        get() = db.isOpen

    //---closes the database---
    fun close() {
        try {
            db.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * insert methods.
     */
    //InsertName is wrapper method, so the activity doesn't have to build  ContentValues for the insert.
    fun insertName(name: String?, value: Int?): Long {
        val initialValues = ContentValues()
        initialValues.put(mySQLiteHelper.KEY_NAME, name)
        initialValues.put(mySQLiteHelper.KEY_SCORE, value)
        return db.insert(mySQLiteHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_FAIL, initialValues)
    }

    //This is the method to actually do an insert using the convenience method.  Note, it uses fail when there is a conflict.
    fun Insert(TableName: String?, values: ContentValues?): Long {
        return db.insert(TableName, SQLiteDatabase.CONFLICT_FAIL, values)
    }

    /**
     * The following a different ways to query the database.  They all return a Cursor.
     */
    //get all the rows.
    val allNames: Cursor?
        get() {
            //SELECT KEY_NAME, KEY_SCORE FROM DATABASE_TABLE SORTBY KEY_NAME;
            val mCursor = qbQuery(
                mySQLiteHelper.TABLE_NAME,  //table name
                arrayOf(                   //projection, ie columns.
                    mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE
                ),
                null,  //selection,  we want everything.
                null,  // String[] selectionArgs,  again, we want everything.
                mySQLiteHelper.KEY_NAME // String sortOrder  by name as the sort.
            )
            if (mCursor != null) //make sure cursor is not empty!
                mCursor.moveToFirst()
            return mCursor
        }

    //Retrieve one entry  METHOD we are supposed to use.
    @Throws(SQLException::class)
    fun get1name(name: String): Cursor? {
        //the query parameter method is not included in supportSQLiteDatabase.
        //So, use the helper function
        val mCursor = qbQuery(
            mySQLiteHelper.TABLE_NAME,
            arrayOf(mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_SCORE),  //projection, ie columns.
            mySQLiteHelper.KEY_NAME + "=\'" + name + "\'",  //selection,
            null,  // String[] selectionArgs,  not necessary here.
            mySQLiteHelper.KEY_NAME // String sortOrder  by name as the sort.
        )
        if (mCursor != null) {
            mCursor.moveToFirst()
        }
        return mCursor
    }

    //retrieve one entry, using RAW method  (ie sql statement)
    fun get1nameR(name: String): Cursor? {
        //public Cursor query (String sql, Object[] bindArgs)
        //sql 	the SQL query. The SQL string must not be ; terminated
        //BindArgs 	You may include ?s in where clause in the query, which will be replaced by the values from selectionArgs. The values will be bound as Strings.
        val mCursor = db.query("select Name, Score from HighScore where Name=\'$name\'", null)
        mCursor?.moveToFirst()
        return mCursor
    }

    //this one uses the supportQueryBuilder that build a SupportSQLiteQuery for the query.
    fun qbQuery(
        TableName: String, projection: Array<String?>?, selection: String?,
        selectionArgs: Array<String?>?, sortOrder: String?
    ): Cursor {
        val qb = SupportSQLiteQueryBuilder.builder(TableName)
        qb.columns(projection)
        qb.selection(selection, selectionArgs)
        qb.orderBy(sortOrder)
        //using the query builder to manage the actual query at this point.
        return db.query(qb.create())
    }

    /**
     * The following are update methods examples.
     */
    // This is a wrapper method, so that main code doesn't have ContentValues.
    fun updateRow(name: String, score: Int): Boolean {
        val args = ContentValues()
        args.put(mySQLiteHelper.KEY_SCORE, score)
        //returns true if one or more updates happened, otherwise false.
        return Update(
            mySQLiteHelper.TABLE_NAME, args,
            mySQLiteHelper.KEY_NAME + "= \'" + name + "\'", null
        ) > 0
    }

    // this is a generic method to update something from the database, uses the Convenience method.
    fun Update(
        TableName: String, values: ContentValues?, selection: String?,
        selectionArgs: Array<String?>?
    ): Int {
        return db.update(TableName, SQLiteDatabase.CONFLICT_FAIL, values, selection, selectionArgs)
    }

    /**
     * the following are delete methods
     */
    // this uses the Convenience method to delete something from the database.
    fun Delete(TableName: String?, selection: String?, selectionArgs: Array<String?>?): Int {
        return db.delete(TableName, selection, selectionArgs)
    }

    //remove all entries from the CurrentBoard
    fun emptydb() {
        db.delete(mySQLiteHelper.TABLE_NAME, null, null)
    }

    //constructor
    init {
        val factory: SupportSQLiteOpenHelper.Factory = FrameworkSQLiteOpenHelperFactory()
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(
            ctx!!
        )
            .name(mySQLiteHelper.DATABASE_NAME)
            .callback(mySQLiteHelper())
            .build()
        helper = factory.create(configuration)
    }
}