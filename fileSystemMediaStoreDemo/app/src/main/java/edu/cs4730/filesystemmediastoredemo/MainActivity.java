package edu.cs4730.filesystemmediastoredemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * An example of how to use the media store to view pictures.
 * It also attempt to get the lat and long data, assuming it has been granted access.
 * While the documentation says, you only need read access, if this app doesn't have media access,
 * it can't read any pictures.
 */

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_PERM_ACCESS = 1;
    Button btn;
    ImageView iv;
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
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.picker);
        iv = findViewById(R.id.imageView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listpictures();
            }
        });

        //make sure we have access permissions.
        CheckPerm();

    }


    void listpictures() {
        Uri collection;
        List<Pic> picList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL); //Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; //Video.Media.EXTERNAL_CONTENT_URI;
        }

        //setup the information for the question, project and sortOrder.  we could sort by date.
        String[] projection = new String[]{
            MediaStore.Images.Media._ID, //   Video.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME, // Video.Media.DISPLAY_NAME,
            //MediaStore.Video.Media.DURATION,
            MediaStore.Images.Media.SIZE  //Video.Media.SIZE
        };
        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME; // MediaStore.Images.Media.DATE_ADDED

        //now question the contentprovider for a list of pictures in DCIM and /pictures directory.
        try (Cursor cursor = getApplicationContext().getContentResolver().query(
            collection,
            projection,
            null,  //selection, all of them.
            null, //selectionArgs,
            sortOrder
        )) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.ThemeOverlay_AppCompat_Dialog));
        builder.setTitle("Choose Type:");
        builder.setSingleChoiceItems(items, -1,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    dialog.dismiss();  //the dismiss is needed here or the dialog stays showing.
                    Log.wtf("Picker", "picked " + item + " " + picList.get(item).uri);
                    ContentResolver resolver = getApplicationContext().getContentResolver();

                    try (InputStream stream = resolver.openInputStream(picList.get(item).uri)) {
                        iv.setImageBitmap(BitmapFactory.decodeStream(stream));
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
                            float[] returnedLatLong = new float[2];
                            if (exifInterface.getLatLong(returnedLatLong)) {
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


    public void CheckPerm() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //I'm on not explaining why, just asking for permission.
            Log.v(TAG, "asking for permissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                MainActivity.REQUEST_PERM_ACCESS);
        } else {
            Log.wtf(TAG, "Contact Write Access: Granted");
        }
    }

    //handle the response.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERM_ACCESS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                Log.wtf(TAG, "ACCESS_MEDIA_LOCATION: Granted");
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Log.wtf(TAG, "ACCESS_MEDIA_LOCATION: Not Granted");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

