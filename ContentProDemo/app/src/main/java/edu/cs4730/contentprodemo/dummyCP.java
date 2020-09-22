package edu.cs4730.contentprodemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import edu.cs4730.contentprodemo.data.Cube;
import edu.cs4730.contentprodemo.data.Square;

/**
 * This example is to provide the very basics of a content provider.  It only returns
 * information, and ignores any attempts to updates/insert/delete.  The basic structure is here and
 * is intended for demonstration purposes.
 * <
 * This has two "tables".  it can provide squares or cubes
 *
 * For a more complete and generic content provider connected to a database, please the sqlite repo,
 * myDBcontentProvider for more information.
 *
 * Note if calling from another applications, it will need to have
 *    <queries> <package android:name="edu.cs4730.contentprodemo" /> </queries> in the manifest file
 *    starting in API 30 for package visibility additions.
 */

public class dummyCP extends ContentProvider {
    public static final String PROVIDER_NAME = "edu.cs4730.provider";

    //these two are not used, but normally handly to have avialabe in a normal provider.
    public static final Uri CONTENT_URI1 =
        Uri.parse("content://" + PROVIDER_NAME + "/square");
    public static final Uri CONTENT_URI2 =
        Uri.parse("content://" + PROVIDER_NAME + "/cube");

    private static final int SQUARE = 1;
    private static final int SQUARE_ID = 2;
    private static final int CUBE = 3;
    private static final int CUBE_ID = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "square", SQUARE);
        uriMatcher.addURI(PROVIDER_NAME, "square/#", SQUARE_ID);
        uriMatcher.addURI(PROVIDER_NAME, "cube", CUBE);
        uriMatcher.addURI(PROVIDER_NAME, "cube/#", CUBE_ID);
    }

    static final String TAG = "dummyCP";
    Cube myCube;
    Square mySquare;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        //nothing really to do here.
        myCube = new Cube();
        mySquare = new Square();
        return true;
    }


    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType");
        switch (uriMatcher.match(uri)) {
            // get all rows
            case SQUARE:
                return "vnd.android.cursor.dir/vnd.cs4730.square";
            // get a particular row
            case SQUARE_ID:
                return "vnd.android.cursor.item/vnd.cs4730.square";
            // get all rows for cube
            case CUBE:
                return "vnd.android.cursor.dir/vnd.cs4730.cube";
            // get a particular row for cube
            case CUBE_ID:
                return "vnd.android.cursor.item/vnd.cs4730.cube";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String stuff;

        Log.d(TAG, "query");
        switch (uriMatcher.match(uri)) {
            case SQUARE_ID:  //get one row from the square.
                stuff = uri.getPathSegments().get(1);
                Log.d(TAG, "stuff is :" + stuff + ":");
                if (stuff != null) {
                    int val = Integer.parseInt(stuff);  //convert from String to int.
                    Log.d(TAG, "query val is " + val);

                    return mySquare.getone(val);
                }
                break;
            case SQUARE:  //get all the rows of square
                Log.d(TAG, "query all!");
                return mySquare.getall();

            case CUBE_ID:  //get one row of cube
                stuff = uri.getPathSegments().get(1);
                if (stuff != null) {
                    int val = Integer.parseInt(stuff);  //convert from String to int.
                    return myCube.getone(val);
                }
                break;
            case CUBE: //get all the rows of cube
                return myCube.getall();
        }
        Log.d(TAG, "query null...");
        //something else, just return null or maybe should throw an exception?
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update");
        // ignore, return default
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // ignore, return default
        Log.d(TAG, "insert");
        return null;
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // ignore, return default
        Log.d(TAG, "delete");
        return 0;
    }


}
