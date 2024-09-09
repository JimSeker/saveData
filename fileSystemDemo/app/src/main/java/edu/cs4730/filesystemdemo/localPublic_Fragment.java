package edu.cs4730.filesystemdemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.cs4730.filesystemdemo.databinding.FragmentLocalpublicBinding;


/**
 * This fragment will write (append) to a file to the local public area of the app.
 * then read back whatever is the file and display it to the screen.
 * uses the DataOutputStream/InputStream to read and write
 * <p>
 * This uses older example code,
 */

public class localPublic_Fragment extends Fragment {
    FragmentLocalpublicBinding binding;
    String TAG = "localp";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView");
        binding = FragmentLocalpublicBinding.inflate(inflater, container, false);

        binding.button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loggerpub.setText("Output:\n");
                localfile();
            }
        });

        return binding.getRoot();
    }

    public void localfile() {
        //make sure the directories exist.
        File datafiledir = requireActivity().getExternalFilesDir(null);
        datafiledir.mkdirs();
        File datafile = new File(datafiledir, "myfiledata.txt");
        //if the file exist, append, else create the file.
        if (datafile.exists()) {
            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(datafile, true));
                dos.writeUTF("Next line\n");
                dos.close();
                binding.loggerpub.append("Wrote next line to file\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else { //file doesn't exist
            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(datafile));  //no append
                dos.writeUTF("first line\n");
                dos.close();
                binding.loggerpub.append("Write first line to file\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //now read it back.
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(datafile));
            while (true) try {
                binding.loggerpub.append(dis.readUTF());
            } catch (EOFException e) {  //reach end of file
                dis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //now do the same, except use the download directory.
        binding.loggerpub.append("\nDownload file:\n");
        File dlfiledir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        dlfiledir.mkdirs();
        File dlfile = new File(dlfiledir, "myfiledl.txt");
        if (dlfile.exists()) {
            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(dlfile, true));
                dos.writeUTF("2Next line\n");
                dos.close();
                binding.loggerpub.append("Wrote next line to file\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else { //file doesn't exist
            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(dlfile));  //no append
                dos.writeUTF("1first line\n");
                dos.close();
                binding.loggerpub.append("Write first line to file\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //now read it back.
        binding.loggerpub.append("Now reading it back \n");
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(dlfile));
            while (true) try {
                binding.loggerpub.append(dis.readUTF());
            } catch (EOFException e) {  //reach end of file
                dis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
