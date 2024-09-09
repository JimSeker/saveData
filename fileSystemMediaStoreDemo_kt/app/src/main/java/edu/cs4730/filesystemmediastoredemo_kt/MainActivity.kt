package edu.cs4730.filesystemmediastoredemo_kt

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import edu.cs4730.filesystemmediastoredemo_kt.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.IOException

/**
 * An example of how to use the media store to view pictures.
 * It also attempt to get the lat and long data, assuming it has been granted access.
 * While the documentation says, you only need read access, if this app doesn't have media access,
 * it can't read any pictures.
 */


class MainActivity : AppCompatActivity() {
    private lateinit var REQUIRED_PERMISSIONS: Array<String>
    private lateinit var rpl: ActivityResultLauncher<Array<String>>
    private lateinit var binding: ActivityMainBinding

    private val TAG: String = "MainActivity"

    //class used to hold information about the picture.  I actually never display size, but you could.
    class Pic(val uri: Uri, internal val name: String, private val size: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        //permissions changes between 28, 32, 33, and 34 too.
        //https://developer.android.com/about/versions/13/behavior-changes-13
        //https://developer.android.com/about/versions/14/changes/partial-photo-video-access
        REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        //setup for the read permissions needed.
        rpl = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            var granted = true
            for ((key, value) in isGranted) {
                logthis("$key is $value")
                if (!value) granted = false
            }
            if (granted) logthis("all permissions granted.")
        }

        binding.picker.setOnClickListener { listpictures() }

        if (!allPermissionsGranted()) {
            rpl.launch(REQUIRED_PERMISSIONS)
        }
    }


    fun listpictures() {
        val picList: MutableList<Pic> = ArrayList()
        //api 29+
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        //api 28 and below uses, but this example is set to API 29+
        //val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; //Video.Media.EXTERNAL_CONTENT_URI;

        //setup the information for the question, project and sortOrder.  we could sort by date.
        val projection = arrayOf(
            MediaStore.Images.Media._ID,  //   Video.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,  // Video.Media.DISPLAY_NAME,
            //MediaStore.Video.Media.DURATION,
            MediaStore.Images.Media.SIZE //Video.Media.SIZE
        )
        val sortOrder = MediaStore.Images.Media.DISPLAY_NAME // MediaStore.Images.Media.DATE_ADDED

        //now question the contentprovider for a list of pictures in DCIM and /pictures directory.
        try {
            applicationContext.contentResolver.query(
                collection, projection, null,  //selection, all of them.
                null,  //selectionArgs,
                sortOrder
            ).use { cursor ->
                Log.wtf("query", "Starting")
                // Cache column indices.
                val idColumn = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val size = cursor.getInt(sizeColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    Log.wtf(
                        "listing", "$id $contentUri $name $size"
                    )

                    picList.add(Pic(contentUri, name, size))
                }
                Log.wtf("query", "ending")
                //launch a dialogbox to pic a picture to display.
                showlistdialog(picList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * This shows a list and the use is to select one of them.
     * Note, this dialog doesn't set a cancel listener, like the one above.  so if the user
     * cancels, nothing happens.
     */
    fun showlistdialog(picList: List<Pic>) {
        val items = arrayOfNulls<String>(picList.size)
        var i = 0
        for (pic in picList) {
            items[i] = pic.name
            i++
        }

        //for thumbnails, use something like this:
        //Bitmap thumbnail = getApplicationContext().getContentResolver().loadThumbnail(content-uri, new Size(640, 480), null);
        val builder = AlertDialog.Builder(
            ContextThemeWrapper(
                this, com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Dialog
            )
        )
        builder.setTitle("Choose Type:")
        builder.setSingleChoiceItems(
            items, -1
        ) { dialog, item ->
            dialog.dismiss() //the dismiss is needed here or the dialog stays showing.
            Log.wtf("Picker", "picked " + item + " " + picList[item].uri)
            val resolver = applicationContext.contentResolver

            try {
                resolver.openInputStream(picList[item].uri).use { stream ->
                    binding.imageView.setImageBitmap(BitmapFactory.decodeStream(stream))
                    // Perform operations on "stream".
                    stream!!.close()
                }
            } catch (e: Exception) {
                Log.wtf("loader", "failed")
                e.printStackTrace()
            }
            // now check if it has any meta data like location.
            // Exception occurs if ACCESS_MEDIA_LOCATION permission isn't granted.
            val photoUri = MediaStore.setRequireOriginal(picList[item].uri)
            try {
                val stream = contentResolver.openInputStream(photoUri)
                if (stream != null) {
                    val exifInterface = ExifInterface(stream)
                    val returnedLatLong = exifInterface.latLong
                    if (returnedLatLong != null) {
                        Log.wtf(
                            "LatLong", "Photo coor " + returnedLatLong[0] + "," + returnedLatLong[1]
                        )
                    } else {
                        Log.wtf("LatLong", "Photo doesn't have lat and long data.")
                    }
                    // Don't reuse the stream associated with
                    // the instance of "ExifInterface".
                    stream.close()
                } else {
                    // Failed to load the stream, so return the coordinates (0, 0).
                    Log.wtf("LatLong", "Failed open Photo ")
                }
            } catch (e: FileNotFoundException) {
                Log.wtf("latlog", "file not found.")
                e.printStackTrace()
            } catch (e: IOException) {
                Log.wtf("latlog", "LatLon not found.")
                e.printStackTrace()
            }
        }
        builder.show()
    }

    /**
     * helper method to log to screen and to logcat.
     */
    fun logthis(item: String?) {
        Log.d(TAG, item!!)
//        logger.append("\n" + item);
    }

    //check we have  permissions.
    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}