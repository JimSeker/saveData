package edu.cs4730.screenshotdeetectordemo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import edu.cs4730.screenshotdeetectordemo.databinding.ActivityMainBinding;

/**
 * attempts to detect screenshots.   It works, but there are likely still issues.
 * on a pixel 4a, using the buttons while the app is up, works.
 * on a pixel 4a, using the buttons while the apps is showing, but not full screen it works
 * on a pixel 4a. app is showing, but smaller.  using the screenshot button on screen. doesn't work.
 * <p>
 * based on some code from here. https://proandroiddev.com/detect-screenshots-in-android-7bc4343ddce1
 * heavily updated to for new permissions and simpler version of their example.
 */

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<String[]> rpl;
    private String[] REQUIRED_PERMISSIONS;
    static String TAG = "MainActivity";
    ContentObserver contentObserver;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        //permissions changes between 28, 32, and 33
        //https://developer.android.com/about/versions/13/behavior-changes-13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED};
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //we actually don't need the media_location, unless you open the file.  I've set the no location, but likely won't work.
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_MEDIA_IMAGES};
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }

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
                        logthis("All permissions granted");
                }
            }
        );


        if (!allPermissionsGranted()) {
            rpl.launch(REQUIRED_PERMISSIONS);
        }

        //get a contentObserver setup, it's registered in onstart, and removed in onstop.
        //this allows to watch for new files, since we can't detect the screenshot it's self, we
        // are looking for the file it produced.
        contentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange, @Nullable Uri uri) {
                super.onChange(selfChange, uri);
                if (selfChange) return;
                //  logthis("new file?" + uri.getPath());
                getname(uri);
            }
        };

    }

    /**
     * This is a smiple method to using the uri to find the name of the file and display it
     * there is also code to load the image and place in imageview (not in the layout), which is commented out.
     */

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

    //Onstop remove the filecontent observer
    protected void onStop() {
        super.onStop();
        getContentResolver().unregisterContentObserver(contentObserver);
        logthis("Stop");
    }

    //Onstart add the filecontent observer
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

    //simple helper function to log information to two places.
    public void logthis(String msg) {
        binding.logger.append(msg + "\n");
        Log.d(TAG, msg);
    }

}