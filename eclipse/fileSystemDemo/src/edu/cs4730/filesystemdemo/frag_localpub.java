package edu.cs4730.filesystemdemo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/*
 * This fragment will write (append) to a file to the local public area of the app.
 * then read back whatever is the file and display it to the screen.
 * 
 * 
 * uses the DataOutputStream/InputStream to read and write
 * 
 * For bufferedWriter/reader example, see frag_ext.java
 */

public class frag_localpub extends Fragment {
	TextView logger;
	String TAG = "localp";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "OnCreateView");
		View view = inflater.inflate(R.layout.frag_localpub, container, false);
		logger = (TextView) view.findViewById(R.id.loggerpub);

		
		view.findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				logger.setText("Output:\n");
				localfile();
			}
		});
		
		return view;
	}
	
	public void localfile() {
		//make sure the directories exist.
		File datafiledir = getActivity().getExternalFilesDir(null);
		datafiledir.mkdirs();
		File datafile = new File(datafiledir, "myfiledata.txt");
		//if the file exist, append, else create the file.
		if (datafile.exists()) {
			try {
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(datafile,true));
				dos.writeUTF("Next line\n");
				dos.close();
				logger.append("Wrote next line to file\n");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else { //file doesn't exist
			try {
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(datafile));  //no append
				dos.writeUTF("first line\n");
				dos.close();
				logger.append("Write first line to file\n");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//now read it back.
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(datafile) );
			while(true)
				try {
					logger.append(in.readUTF());
				} catch (EOFException e) {  //reach end of file
					in.close();
				}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		
		//now do the same, except use the download directory.
		logger.append("\nDownload file:\n");
		File dlfiledir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
		dlfiledir.mkdirs();
		File dlfile = new File(dlfiledir, "myfiledl.txt");
		if (dlfile.exists()) {
			try {
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(dlfile,true));
				dos.writeUTF("2Next line\n");
				dos.close();
				logger.append("Wrote next line to file\n");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else { //file doesn't exist
			try {
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(dlfile));  //no append
				dos.writeUTF("1first line\n");
				dos.close();
				logger.append("Write first line to file\n");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//now read it back.
		logger.append("Now reading it back \n");
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(dlfile) );
			while(true)
				try {
					logger.append(in.readUTF());
				} catch (EOFException e) {  //reach end of file
					in.close();
				}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

}
