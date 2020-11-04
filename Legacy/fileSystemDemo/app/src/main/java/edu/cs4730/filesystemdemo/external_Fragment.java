package edu.cs4730.filesystemdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;


/**
 * This fragment will write (append) to a file on media card
 * then read back whatever is the file and display it to the screen.
 * <p>
 * uses bufferedWriter/reader
 * older code using the DataOutputStream is commented out, but left as an example
 * of how to use it.
 *
 * This example will work in API 30 using the  Manifest.permission.MANAGE_EXTERNAL_STORAGE, but the
 * permission doesn't seem to grant everything and the user must manually turn on some things.
 * plus the permission requires android/google's permission to use.
 *   https://support.google.com/googleplay/android-developer/answer/9956427  so this is really need a valid
 *  way to do this, unless you plan to write a file manager, or something that google would approve.
 *
 */

public class external_Fragment extends Fragment {
    TextView logger;
    String TAG = "ext";
    MainActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView");
        View view = inflater.inflate(R.layout.fragment_external, container, false);
        logger = view.findViewById(R.id.loggerext);
        parent = (MainActivity) getActivity();

        view.findViewById(R.id.button3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.setText("Output:\n");
                CheckPerm();
            }
        });

        return view;
    }

    public void CheckPerm() {
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            //I'm on not explaining why, just asking for permission.
            Log.v(TAG, "asking for permissions");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                MainActivity.REQUEST_PERM_ACCESS);

        } else {
            logger.append("\nContact Write Access: Granted\n");
            extfolder();
        }

    }

    public void extfolder() {
        logger.append("\nOn to external storage\n");
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        //first make sure we can read the media card
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        if (mExternalStorageAvailable) {
            logger.append("Media is available and ");
            if (mExternalStorageWriteable)
                logger.append("writable\n");
            else
                logger.append("Read only\n");

        } else {
            logger.append("Media is not available\n");
        }
        if (mExternalStorageWriteable) {
            //now write/append to the file "Hi There\n"
            File extdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            extdir.mkdir(); //just in case it doesn't exist yet.
            File file = new File(extdir, "Myfiletest.txt");
            try {
                BufferedWriter bW = new BufferedWriter(new FileWriter(file, true));
                bW.write("Hi There");
                bW.newLine();
                bW.flush();
                bW.close();
                /*
				FileOutputStream f = new FileOutputStream(file, true);  //append!  false to overwrite.
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(f));
				out.writeUTF("Hi There\n");
	    		out.close();
	    		*/
                logger.append("Wrote a line to the file myfiletest in downloads");
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Can create file: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "write failure: " + e.getMessage());
            }
        }
        logger.append("\nReading back data:\n");
        //now read back whatever is in the file.
        if (mExternalStorageAvailable) {
            File extdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            extdir.mkdir(); //just in case it doesn't exist yet, but it should, from before.
            File file = new File(extdir, "Myfiletest.txt");
            String line = "";
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                line = in.readLine();
                while (line != null) {
                    logger.append(line + "\n");
                    line = in.readLine();
                }
                in.close();
                in = null;
    			/*
				FileInputStream f = new FileInputStream(file);
				DataInputStream in = new DataInputStream(new BufferedInputStream(f));
				while(true)
					try {
						logger.append(in.readUTF() + "\n");
					} catch (EOFException e) {  //reach end of file
						in.close();
					}
					*/
            } catch (FileNotFoundException e) {
                Log.d(TAG, "file not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "IO error: " + e.getMessage());
            }
        }

    }
}
