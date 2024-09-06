package edu.cs4730.contentprosqlitedbdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import androidx.annotation.NonNull;
import edu.cs4730.contentprosqlitedbdemo.db.ScoreDatabase;
import edu.cs4730.contentprosqlitedbdemo.db.mySQLiteHelper;


public class myDBContentProvider extends ContentProvider {

	/*
     * used the following for a lot of information
	 * http://thinkandroid.wordpress.com/2010/01/13/writing-your-own-contentprovider/
	 * 
	 * We could use the ScoreDatabase, but it is not very generic and we need to create
	 * generic methods to handle some of the requests, instead just creating them here.
	 * Plus need  to add stuff for the loader classes as well.
	 * 
	 * As a note, this provides a nearly generic content provider for any database.
	 * specific database names, columns would need to be changed, but otherwise, should
	 * work for most databases.
	 *
	 * Note there is only one table, so the provider only has score and score_id
	 * If there were more tables publicly accessible, then we would need more names/numbers for
	 * urimatcher.
	 */

    public static final String PROVIDER_NAME = "edu.cs4730.scoreprovider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + PROVIDER_NAME + "/score");

    private static final int SCORE = 1;
    private static final int SCORE_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "score", SCORE);
        uriMatcher.addURI(PROVIDER_NAME, "score/#", SCORE_ID);
    }

    static final String TAG = "myDBCP";

    //the database to be used in the contenProvider
    ScoreDatabase db;


    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            // get all rows
            case SCORE:
                return "vnd.android.cursor.dir/vnd.cs4730.score";
            // get a particular row
            case SCORE_ID:
                return "vnd.android.cursor.item/vnd.cs4730.score";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        db = new ScoreDatabase(requireContext());
        db.open();
        return true;
    }

    /**
     * So delete is a simple function
     *   Remember, contentproviders should be used a a wrapper class, so it calls into the ScoreDatabase
     *   to do the work.  But it does need setup the selection arg correctly.
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {
            case SCORE:
                break;
            case SCORE_ID:
                selection = fixit(selection, "_id = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.Delete(mySQLiteHelper.TABLE_NAME, selection, selectionArgs);
        requireContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /*
     * 	For the query method, but need to provide a couple more piece of information to the query
     * but are null values, plus pass onthe sortOrder on to it.
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (uriMatcher.match(uri)) {
            case SCORE:
                break;
            case SCORE_ID:
                selection = fixit(selection, "_id = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor c = db.qbQuery(mySQLiteHelper.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
        //this line is added for the observers.  if we  changed the database, this allows a notification to be set.
        c.setNotificationUri(requireContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != SCORE) {  //can't insert by id number, so only generic is allowed.
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (values == null) {   //basic error checking.  values is null, provider an empty one
            values = new ContentValues();  //or we could through an SQLExecption as well.
        }


        long rowId = db.Insert(mySQLiteHelper.TABLE_NAME, values);
        if (rowId > 0) {   //add the row id to the uri and return it to the user.
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            //this line is added for the observers.  We changed the database, so notify everyone.
            requireContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count;
        switch (uriMatcher.match(uri)) {
            case SCORE:
                break;

            case SCORE_ID:
                selection = fixit(selection, "_id = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        count = db.Update(mySQLiteHelper.TABLE_NAME, values, selection, selectionArgs);
        //this line is added for the observers.  We changed the database, so notify everyone.
        requireContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /*
      * this is intended to deal with null and ANDs needed int he selection string value
     */

    private String fixit(String tofix, String item) {
        if (tofix == null) {
            tofix = item;
        } else if (tofix.compareTo("") ==0) {
            tofix = item;
        }else {
            tofix += " AND " + item;
        }
        return tofix;
    }


}
