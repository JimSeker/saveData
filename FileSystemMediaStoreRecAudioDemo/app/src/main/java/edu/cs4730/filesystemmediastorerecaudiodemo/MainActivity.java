package edu.cs4730.filesystemmediastorerecaudiodemo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.cs4730.filesystemmediastorerecaudiodemo.databinding.ActivityMainBinding;
import edu.cs4730.filesystemmediastorerecaudiodemo.databinding.LayoutDialogBinding;

public class MainActivity extends AppCompatActivity {
    final String TAG = "mainactivity";
    private String[] REQUIRED_PERMISSIONS;
    ActivityResultLauncher<String[]> rpl;

    boolean recording = false;
    MediaRecorder audioRecorder;
    MediaPlayer mPlayer = null;
    Uri audiouri;
    ParcelFileDescriptor file;
    String fileName;

    ActivityMainBinding binding;

    class Audio {
        private final Uri uri;
        private final String name;
        private final int duration;
        private final int size;

        public Audio(Uri uri, String name, int duration, int size) {
            this.uri = uri;
            this.name = name;
            this.duration = duration;
            this.size = size;
        }
    }


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
        setSupportActionBar(binding.toolbar);

        fileName = "mediastoreRectest.mp3";

        //permissions changes between 28, 32, and 33
        //https://developer.android.com/about/versions/13/behavior-changes-13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED};
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_MEDIA_AUDIO};
        } else {  //if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
        } //else { //28 and below.
        //    REQUIRED_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        //}

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


        binding.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recording) {
                    //start recording

                    try {
                        startRecording();
                        binding.record.setText("Stop Recording");
                        recording = true;
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Failed to start", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //stop recording
                    stopRecording();
                    binding.record.setText("Start Recording");
                    recording = false;
                }
            }
        });
        findViewById(R.id.pickname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        findViewById(R.id.playfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listmp3s();
            }
        });

        if (!allPermissionsGranted()) {
            rpl.launch(REQUIRED_PERMISSIONS);
        }
    }

    /**
     * Start recording a file, based on the filename entered (or default) in the Music/recording subdirectory
     *
     * @throws IOException
     */
    void startRecording() throws IOException {
        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Audio.Media.TITLE, fileName);// not needed?
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/");  //api 29 and up only.

        audiouri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        file = getContentResolver().openFileDescriptor(audiouri, "w");

        if (file != null) {
            audioRecorder = new MediaRecorder(this);
            audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            audioRecorder.setOutputFile(file.getFileDescriptor());
            audioRecorder.setAudioChannels(1);
            audioRecorder.prepare();
            audioRecorder.start();
        }
    }

    /**
     * stop recording.
     */
    void stopRecording() {
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
        }
    }

    /**
     * enter a file name via a dialog.
     */
    public void showInputDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutDialogBinding dialogBinding = LayoutDialogBinding.inflate(inflater);
        dialogBinding.itemAdded.setText(fileName);
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_dialog));
        builder.setView(dialogBinding.getRoot()).setTitle("Enter a file name");
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                fileName = dialogBinding.itemAdded.getText().toString();
                logthis("using new filename: " + fileName);
                //Toast.makeText(getBaseContext(), userinput.getText().toString(), Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logthis("Dialog canceled, using original filename: " + fileName);
                dialog.cancel();
            }
        });
        builder.show();
    }

    void listmp3s() {
        Uri collection;
        String[] projection;
        String selection;
        String[] selectionArgs;
        List<Audio> audioList = new ArrayList<>();

        collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL); //Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        //setup the information for the question, project and sortOrder.  we could sort by date.
        projection = new String[]{MediaStore.Audio.Media._ID, //   Video.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME, // Video.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.RELATIVE_PATH,  //must be API 29 in order to use.
            MediaStore.Audio.Media.SIZE  //Video.Media.SIZE
        };
        String audiodir = "Music/Recordings/%";
        selection = MediaStore.Audio.Media.RELATIVE_PATH + " like ?";
        selectionArgs = new String[]{audiodir};

        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME; // MediaStore.Images.Media.DATE_ADDED

        //now question the contentprovider for a list of pictures in DCIM and /pictures directory.
        try (Cursor cursor = getApplicationContext().getContentResolver().query(collection, projection, selection,  //selection, all of them.
            selectionArgs, //selectionArgs,
            sortOrder)) {
            Log.wtf("query", "Starting");
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);
                int duration = cursor.getInt(durationColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                Log.wtf("listing", id + " " + contentUri + " " + name + " " + duration + " " + size);

                audioList.add(new Audio(contentUri, name, duration, size));
            }
            Log.wtf("query", "ending");
            //launch a dialogbox to pic a picture to display.
            showlistdialog(audioList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showlistdialog(List<Audio> audioList) {
        String[] items = new String[audioList.size()];
        int i = 0;
        for (Audio audio : audioList) {
            items[i] = audio.name;
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_dialog));
        builder.setTitle("Choose Type:");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();  //the dismiss is needed here or the dialog stays showing.
                logthis("picked " + item + " " + audioList.get(item).uri);
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(getApplicationContext(), audioList.get(item).uri);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.show();

//to use the file descriptor switch to this code.
//        // Open a specific media item using ParcelFileDescriptor.
//        ContentResolver resolver = getApplicationContext().getContentResolver();
//        String readOnlyMode = "r";
//        try (
//            ParcelFileDescriptor pfd = resolver.openFileDescriptor(audioList.get(0).uri, readOnlyMode)) {
//            // Perform operations on "pfd".
//            mPlayer.setDataSource(pfd.getFileDescriptor());
//            mPlayer.prepare();
//            mPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * helper method to log to screen and to logcat.
     */
    void logthis(String item) {
        Log.d(TAG, item);
        binding.logger.append("\n" + item);
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