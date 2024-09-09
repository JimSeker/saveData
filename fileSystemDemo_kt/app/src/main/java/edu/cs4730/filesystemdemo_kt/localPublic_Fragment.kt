package edu.cs4730.filesystemdemo_kt

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.cs4730.filesystemdemo_kt.databinding.FragmentLocalpublicBinding
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files

/**
 * This fragment will write (append) to a file to the local public area of the app.
 * then read back whatever is the file and display it to the screen.
 * uses the DataOutputStream/InputStream to read and write
 *
 *
 * This uses older example code,
 */
@SuppressLint("SetTextI18n")
class localPublic_Fragment : Fragment() {
    private lateinit var binding: FragmentLocalpublicBinding
    private var TAG: String = "localp"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "OnCreateView")
        binding = FragmentLocalpublicBinding.inflate(inflater, container, false)

        binding.button2.setOnClickListener {
            binding.loggerpub.text = "Output:\n"
            localfile()
        }

        return binding.root
    }

    fun localfile() {
        //make sure the directories exist.
        val datafiledir = requireActivity().getExternalFilesDir(null)
        datafiledir!!.mkdirs()
        val datafile = File(datafiledir, "myfiledata.txt")
        //if the file exist, append, else create the file.
        if (datafile.exists()) {
            try {
                val dos = DataOutputStream(FileOutputStream(datafile, true))
                dos.writeUTF("Next line\n")
                dos.close()
                binding.loggerpub.append("Wrote next line to file\n")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else { //file doesn't exist
            try {
                val dos = DataOutputStream(Files.newOutputStream(datafile.toPath())) //no append
                dos.writeUTF("first line\n")
                dos.close()
                binding.loggerpub.append("Write first line to file\n")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //now read it back.
        try {
            val dis = DataInputStream(Files.newInputStream(datafile.toPath()))
            while (true) try {
                binding.loggerpub.append(dis.readUTF())
            } catch (e: EOFException) {  //reach end of file
                dis.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


        //now do the same, except use the download directory.
        binding.loggerpub.append("\nDownload file:\n")
        val dlfiledir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        dlfiledir!!.mkdirs()
        val dlfile = File(dlfiledir, "myfiledl.txt")
        if (dlfile.exists()) {
            try {
                val dos = DataOutputStream(FileOutputStream(dlfile, true))
                dos.writeUTF("2Next line\n")
                dos.close()
                binding.loggerpub.append("Wrote next line to file\n")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else { //file doesn't exist
            try {
                val dos = DataOutputStream(Files.newOutputStream(dlfile.toPath())) //no append
                dos.writeUTF("1first line\n")
                dos.close()
                binding.loggerpub.append("Write first line to file\n")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //now read it back.
        binding.loggerpub.append("Now reading it back \n")
        try {
            val dis = DataInputStream(FileInputStream(dlfile))
            while (true) try {
                binding.loggerpub.append(dis.readUTF())
            } catch (e: EOFException) {  //reach end of file
                dis.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
