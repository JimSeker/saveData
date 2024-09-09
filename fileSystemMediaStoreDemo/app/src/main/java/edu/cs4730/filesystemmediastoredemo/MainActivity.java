package edu.cs4730.filesystemmediastoredemo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cs4730.filesystemmediastoredemo.databinding.ActivityMainBinding;


/**
 * An example of how to use the media store to view pictures.
 * It also attempt to get the lat and long data, assuming it has been granted access.
 * While the documentation says, you only need read access, if this app doesn't have media access,
 * it can't read any pictures.
 */

public class MainActivity extends AppCompatActivity {
    private String[] REQUIRED_PERMISSIONS;
    ActivityResultLauncher<String[]> rpl;
    ActivityMainBinding binding;
    final static String TAG = "MainActivity";

    //class used to hold information about the picture.  I actually never display size, but you could.
    class Pic {
        private final Uri uri;
        private final String name;
        private final int size;

        public Pic(Uri uri, String name, int size) {
            this.uri = uri;
            this.name = name;
            this.size = size;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //permissions changes between 28, 32, 33, and 34 too.
        //https://developer.android.com/about/versions/13/behavior-changes-13
        //https://developer.android.com/about/versions/14/changes/partial-photo-video-access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED};
        }  else  if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        //setup for the read permissions needed.
        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> isGranted) {
                boolean granted = true;
                for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                    logthis(x.getKey() + " is " + x.getValue());
                    if (!x.getValue()) granted = false;
                }
                if (granted) logthis("all permissions granted.");
            }
        });

        binding.picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listpictures();
            }
        });

        if (!allPermissionsGranted()) {
            rpl.launch(REQUIRED_PERMISSIONS);
        }
    }


    void listpictures() {
        Uri collection;
        List<Pic> picList = new ArrayList<>();
        //api 29+
        collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);

        //api 28 and below uses, but this example is set to API 29+
        //collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; //Video.Media.EXTERNAL_CONTENT_URI;

        //setup the information for the question, project and sortOrder.  we could sort by date.
        String[] projection = new String[]{MediaStore.Images.Media._ID, //   Video.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME, // Video.Media.DISPLAY_NAME,
                //MediaStore.Video.Media.DURATION,
                MediaStore.Images.Media.SIZE  //Video.Media.SIZE
        };
        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME; // MediaStore.Images.Media.DATE_ADDED

        //now question the contentprovider for a list of pictures in DCIM and /pictures directory.
        try (Cursor cursor = getApplicationContext().getContentResolver().query(collection, projection, null,  //selection, all of them.
                null, //selectionArgs,
                sortOrder)) {
            Log.wtf("query", "Starting");
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                Log.wtf("listing", id + " " + contentUri + " " + name + " " + size);

                picList.add(new Pic(contentUri, name, size));
            }
            Log.wtf("query", "ending");
            //launch a dialogbox to pic a picture to display.
            showlistdialog(picList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This shows a list and the use is to select one of them.
     * Note, this dialog doesn't set a cancel listener, like the one above.  so if the user
     * cancels, nothing happens.
     */
    void showlistdialog(List<Pic> picList) {
        String[] items = new String[picList.size()];
        int i = 0;
        for (Pic pic : picList) {
            items[i] = pic.name;
            i++;
        }
        //for thumbnails, use something like this:
        //Bitmap thumbnail = getApplicationContext().getContentResolver().loadThumbnail(content-uri, new Size(640, 480), null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.ThemeOverlay_MaterialComponents_Dialog));
        builder.setTitle("Choose Type:");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();  //the dismiss is needed here or the dialog stays showing.
                Log.wtf("Picker", "picked " + item + " " + picList.get(item).uri);
                ContentResolver resolver = getApplicationContext().getContentResolver();

                try (InputStream stream = resolver.openInputStream(picList.get(item).uri)) {
                    binding.imageView.setImageBitmap(BitmapFactory.decodeStream(stream));
                    // Perform operations on "stream".
                    stream.close();
                } catch (Exception e) {
                    Log.wtf("loader", "failed");
                    e.printStackTrace();
                }
                // now check if it has any meta data like location.
                // Exception occurs if ACCESS_MEDIA_LOCATION permission isn't granted.
                Uri photoUri;
                photoUri = MediaStore.setRequireOriginal(picList.get(item).uri);
                try {
                    InputStream stream = getContentResolver().openInputStream(photoUri);
                    if (stream != null) {
                        ExifInterface exifInterface = new ExifInterface(stream);
                        double[] returnedLatLong;
                        returnedLatLong = exifInterface.getLatLong();
                        if (returnedLatLong != null) {
                            Log.wtf("LatLong", "Photo coor " + returnedLatLong[0] + "," + returnedLatLong[1]);
                        } else {
                            Log.wtf("LatLong", "Photo doesn't have lat and long data.");
                        }
                        // Don't reuse the stream associated with
                        // the instance of "ExifInterface".
                        stream.close();
                    } else {
                        // Failed to load the stream, so return the coordinates (0, 0).
                        Log.wtf("LatLong", "Failed open Photo ");
                    }
                } catch (FileNotFoundException e) {
                    Log.wtf("latlog", "file not found.");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.wtf("latlog", "LatLon not found.");
                    e.printStackTrace();
                }


            }
        });
        builder.show();
    }

    /**
     * helper method to log to screen and to logcat.
     */
    void logthis(String item) {
        Log.d(TAG, item);
//        logger.append("\n" + item);
    }

    //check we have  permissions.
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}

