package edu.cs4730.filesystemdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import edu.cs4730.filesystemdemo.databinding.FragmentLocalprivateBinding;

/**
 * This fragment will write (append) to a file to the local private area of the app.
 * then read back whatever is the file and display it to the screen.
 * <p>
 * For bufferedWriter/reader example, see external_Fragment.java
 */

public class localPrivate_Fragment extends Fragment {
    FragmentLocalprivateBinding binding;
    String TAG = "localp";
    MainActivity parent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView");
        binding = FragmentLocalprivateBinding.inflate(inflater, container, false);

       binding.button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loggerlp.setText("Output:\n");
                readwritelocal();
            }
        });

        return binding.getRoot();
    }

    public void readwritelocal() {
        binding.loggerlp.append("check for local files\n");
        String[] flist = requireActivity().fileList();

        if (flist.length == 0) {
            binding.loggerlp.append("No current files storage internally. Creating one\n");
            try {
                OutputStreamWriter osr = new OutputStreamWriter(requireActivity().openFileOutput("FileExample", Context.MODE_PRIVATE));
                BufferedWriter bW = new BufferedWriter(osr);
                bW.write("First line of the file");
                bW.newLine();
                bW.flush();
                bW.close();
                osr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //now the rest of the example will work, since we have created a file.
            flist = requireActivity().fileList();
        }

        String line = "";
        if (flist.length > 0) {
            for (int i = 0; i < flist.length; ++i) {
                binding.loggerlp.append(flist[i] + "\n");

                binding.loggerlp.append("Now appending to it\n");
                try {

                    OutputStreamWriter osr = new OutputStreamWriter(requireActivity().openFileOutput(flist[i], Context.MODE_APPEND));
                    BufferedWriter bW = new BufferedWriter(osr);
                    bW.write("Another Line");
                    bW.newLine();
                    bW.flush();
                    bW.close();
                    osr.close();
                    /* older method, commented out, but left as an example.
                    DataOutputStream dos;
                    dos = new DataOutputStream(requireActivity().openFileOutput(flist[i], Context.MODE_APPEND));
                    dos.writeUTF("Another line");
                    dos.close();
                     */

                } catch (IOException e) { //which includes the FileNotFoundException e
                    e.printStackTrace();
                }

                //now read the file
                try {

                    InputStreamReader isr = new InputStreamReader(requireActivity().openFileInput(flist[i]));
                    BufferedReader in = new BufferedReader(isr);
                    line = in.readLine();
                    while (line != null) {
                        binding.loggerlp.append(line + "\n");
                        line = in.readLine();
                    }
                    in.close();
                    isr.close();

                    /* older method, commented out, but left as an example.
                     DataInputStream in;
                    in = new DataInputStream(requireActivity().openFileInput(flist[i]));
                    while (true)
                        try {
                            binding.loggerlp.append(in.readUTF() + "\n");
                        } catch (EOFException e) {  //reach end of file
                            in.close();
                        }

                      */
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "file not found: " + e.getMessage());
                } catch (IOException e) {//which includes the FileNotFoundException e
                    e.printStackTrace();
                }
            }
        }
    }
}