package edu.cs4730.filesystemmediastorerecaudiodemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String TAG = "mainactivity";
    public static final int REQUEST_PERM_ACCESS = 1;

    Button record;
    boolean recording = false;

    MediaRecorder audioRecorder;
    MediaPlayer mPlayer = null;
    Uri audiouri;
    ParcelFileDescriptor file;
    String fileName;

    TextView logger;

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
        setContentView(R.layout.activity_main);
        logger = findViewById(R.id.logger);
        record = findViewById(R.id.record);

        fileName = "mediastoreRectest.mp3";

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recording) {
                    //start recording

                    try {
                        startRecording();
                        record.setText("Stop Recording");
                        recording = true;
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Failed to start", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //stop recording
                    stopRecording();
                    record.setText("Start Recording");
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
        CheckPerm();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {  //For API 29+ (q), for 26 to 28, it just goes into the music directory.
            values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/");
        }

        audiouri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        file = getContentResolver().openFileDescriptor(audiouri, "w");

        if (file != null) {
            audioRecorder = new MediaRecorder();
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
        final View textenter = inflater.inflate(R.layout.layout_dialog, null);
        final EditText userinput = (EditText) textenter.findViewById(R.id.item_added);
        userinput.setText(fileName);
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_dialog));
        builder.setView(textenter).setTitle("Enter a file name");
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                fileName = userinput.getText().toString();
                logthis("using new filename: " + fileName);
                //Toast.makeText(getBaseContext(), userinput.getText().toString(), Toast.LENGTH_LONG).show();
            }
        })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL); //Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            //setup the information for the question, project and sortOrder.  we could sort by date.
            projection = new String[]{
                MediaStore.Audio.Media._ID, //   Video.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME, // Video.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.RELATIVE_PATH,  //must be API 29 in order to use.
                MediaStore.Audio.Media.SIZE  //Video.Media.SIZE
            };
            String audiodir = "Music/Recordings/%";
            selection = MediaStore.Audio.Media.RELATIVE_PATH + " like ?";
            selectionArgs = new String[]{audiodir};

        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; //Video.Media.EXTERNAL_CONTENT_URI;
            //setup the information for the question, project and sortOrder.  we could sort by date.
            //we are just using the /music directory for 26 to 28.
            projection = new String[]{
                MediaStore.Audio.Media._ID, //   Video.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME, // Video.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                //MediaStore.Audio.Media.RELATIVE_PATH,  //must be API 29 in order to use.
                MediaStore.Audio.Media.SIZE  //Video.Media.SIZE
            };
            //since can't use path, just null these.
            selection = null;  //so all of them.
            selectionArgs = null;
        }
        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME; // MediaStore.Images.Media.DATE_ADDED

        //now question the contentprovider for a list of pictures in DCIM and /pictures directory.
        try (Cursor cursor = getApplicationContext().getContentResolver().query(
            collection,
            projection,
            selection,  //selection, all of them.
            selectionArgs, //selectionArgs,
            sortOrder
        )) {
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
        builder.setSingleChoiceItems(items, -1,
            new DialogInterface.OnClickListener() {
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
        logger.append("\n" + item);
    }


    public void CheckPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                //I'm on not explaining why, just asking for permission.
                Log.v(TAG, "asking for permissions");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.REQUEST_PERM_ACCESS);
            } else {
                Log.wtf(TAG, "record audio and read Access: Granted");
            }
        } else { //need to add the write permissions.
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                //I'm on not explaining why, just asking for permission.
                Log.v(TAG, "asking for permissions");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.REQUEST_PERM_ACCESS);
            } else {
                Log.wtf(TAG, "read audio, write and read external Access: Granted");
            }
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
                Log.wtf(TAG, "Record_audio and read external storage:  granted. ");
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Log.wtf(TAG, "Record_audio and read external storage: Not Granted");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}