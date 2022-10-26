package edu.cs4730.screenshotdeetectordemo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<String[]> rpl;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
    static String TAG = "MainActivity";
    ContentObserver contentObserver;

    TextView logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setup for the read permissions needed.
        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> isGranted) {
                    boolean granted = true;
                    for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                        logthis(x.getKey() + " is " + x.getValue());
                        if (!x.getValue()) granted = false;
                    }
                    if (granted)
                        logthis("Permissions granted for api 33+");
                }
            }
        );


        if (!allPermissionsGranted()) {
            rpl.launch(REQUIRED_PERMISSIONS);
        }

        logger = findViewById(R.id.logger);

        //get a contentObserver setup, it's registered in onresume, and removed in onpause.
        contentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, @Nullable Uri uri) {
                super.onChange(selfChange, uri);
                if (selfChange)  return;
              //  logthis("new file?" + uri.getPath());
                getname(uri);
            }
        };

    }


    public void getname(Uri uri) {
        //setup the information for the question, project and sortOrder.  we could sort by date.
        String[] projection = new String[]{
            MediaStore.Images.Media._ID, //   Video.Media._ID,
            MediaStore.Images.Media.RELATIVE_PATH, // only api 29+
            MediaStore.Images.Media.DISPLAY_NAME, // Video.Media.DISPLAY_NAME,
        };
        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME; // MediaStore.Images.Media.DATE_ADDED
        try (Cursor cursor = getContentResolver().query(
            uri,
            projection,
            null,  //selection, all of them.
            null, //selectionArgs,
            sortOrder
        )) {
            Log.wtf("query", "Starting");
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int relativePath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String path = cursor.getString(relativePath);
                String name = cursor.getString(nameColumn);

                String lowername = name.toLowerCase(Locale.ROOT);
                String lowerpath = path.toLowerCase(Locale.ROOT);
                  Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                if (lowername.contains("screenshot") || lowerpath.contains("screenshot"))
                    logthis(name);
                /*

                //to open and display the file use this code.
                try (InputStream stream = etContentResolver().openInputStream(contentUri)) {
                    iv.setImageBitmap(BitmapFactory.decodeStream(stream));
                    // Perform operations on "stream".
                    stream.close();
                } catch (Exception e) {
                    Log.wtf("loader", "failed");
                    e.printStackTrace();
                }
                */
            }
            Log.wtf("query", "ending");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected void onStop() {
        super.onStop();
        getContentResolver().unregisterContentObserver(contentObserver);
        logthis("Stop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver);
        logthis("Start");
    }


    //ask for permissions when we start.
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void logthis(String msg) {
        logger.append(msg + "\n");
        Log.d(TAG, msg);
    }

}