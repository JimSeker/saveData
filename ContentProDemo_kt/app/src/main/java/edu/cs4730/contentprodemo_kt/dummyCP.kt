package edu.cs4730.contentprodemo_kt

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import edu.cs4730.contentprodemo_kt.data.Cube
import edu.cs4730.contentprodemo_kt.data.Square

/**
 * This example is to provide the very basics of a content provider.  It only returns
 * information, and ignores any attempts to updates/insert/delete.  The basic structure is here and
 * is intended for demonstration purposes.
 *
 * This has two "tables".  it can provide squares or cubes
 *
 * For a more complete and generic content provider connected to a database, please the sqlite repo,
 * myDBcontentProvider_kt for more information.
 *
 * Note if calling from another applications, it will need to have
 * <queries> <package android:name="edu.cs4730.contentprodemo_kt"></package> </queries> in the manifest file
 * starting in API 30 for package visibility additions.
 */
class dummyCP : ContentProvider() {
    private lateinit var myCube: Cube
    private lateinit var mySquare: Square

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate")
        //nothing really to do here.
        myCube = Cube()
        mySquare = Square()
        return true
    }


    override fun getType(uri: Uri): String {
        Log.d(TAG, "getType")
        return when (uriMatcher.match(uri)) {
            SQUARE -> "vnd.android.cursor.dir/vnd.cs4730.square"
            SQUARE_ID -> "vnd.android.cursor.item/vnd.cs4730.square"
            CUBE -> "vnd.android.cursor.dir/vnd.cs4730.cube"
            CUBE_ID -> "vnd.android.cursor.item/vnd.cs4730.cube"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val stuff: String?

        Log.d(TAG, "query")
        when (uriMatcher.match(uri)) {
            SQUARE_ID -> {
                stuff = uri.pathSegments[1]
                Log.d(TAG, "stuff is :$stuff:")
                if (stuff != null) {
                    val value = stuff.toInt() //convert from String to int.
                    Log.d(TAG, "query val is $value")

                    return mySquare.getone(value)
                }
            }

            SQUARE -> {
                Log.d(TAG, "query all!")
                return mySquare.getall()
            }

            CUBE_ID -> {
                stuff = uri.pathSegments[1]
                if (stuff != null) {
                    val value = stuff.toInt() //convert from String to int.
                    return myCube.getone(value)
                }
            }

            CUBE -> return myCube.getall()
        }
        Log.d(TAG, "query null...")
        //something else, just return null or maybe should throw an exception?
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.d(TAG, "update")
        // ignore, return default
        return 0
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // ignore, return default
        Log.d(TAG, "insert")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // ignore, return default
        Log.d(TAG, "delete")
        return 0
    }

    companion object {
        const val PROVIDER_NAME: String = "edu.cs4730.provider_kt"

        //these two are not used, but normally handy to have available in a normal provider.
        val CONTENT_URI1: Uri = Uri.parse("content://" + PROVIDER_NAME + "/square")
        val CONTENT_URI2: Uri = Uri.parse("content://" + PROVIDER_NAME + "/cube")

        private const val SQUARE = 1
        private const val SQUARE_ID = 2
        private const val CUBE = 3
        private const val CUBE_ID = 4

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(PROVIDER_NAME, "square", SQUARE)
            uriMatcher.addURI(PROVIDER_NAME, "square/#", SQUARE_ID)
            uriMatcher.addURI(PROVIDER_NAME, "cube", CUBE)
            uriMatcher.addURI(PROVIDER_NAME, "cube/#", CUBE_ID)
        }

        const val TAG: String = "dummyCP"
    }
}
