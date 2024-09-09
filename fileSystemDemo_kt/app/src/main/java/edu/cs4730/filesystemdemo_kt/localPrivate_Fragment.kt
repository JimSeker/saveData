package edu.cs4730.filesystemdemo_kt

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.cs4730.filesystemdemo_kt.databinding.FragmentLocalprivateBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.EOFException
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * This fragment will write (append) to a file to the local private area of the app.
 * then read back whatever is the file and display it to the screen.
 *
 * For bufferedWriter/reader example, see external_Fragment.java
 */

@SuppressLint("SetTextI18n")
class localPrivate_Fragment : Fragment() {
    private lateinit var binding: FragmentLocalprivateBinding
    private var TAG: String = "localp"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "OnCreateView")
        binding = FragmentLocalprivateBinding.inflate(inflater, container, false)

        binding.button1.setOnClickListener {
            binding.loggerlp.text = "Output:\n"
            readwritelocal()
        }

        return binding.root
    }

    fun readwritelocal() {
        binding.loggerlp.append("check for local files\n")
        var flist = requireActivity().fileList()

        if (flist.isEmpty()) {
            binding.loggerlp.append("No current files storage internally. Creating one\n")
            try {
                val osr = OutputStreamWriter(
                    requireActivity().openFileOutput("FileExample", Context.MODE_PRIVATE)
                )
                val bW = BufferedWriter(osr)
                bW.write("First line of the file")
                bW.newLine()
                bW.flush()
                bW.close()
                osr.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //now the rest of the example will work, since we have created a file.
            flist = requireActivity().fileList()
        }

        var line: String?
        if (flist.isNotEmpty()) {
            for (i in flist.indices) {
                binding.loggerlp.append(flist[i] + "\n")

                binding.loggerlp.append("Now appending to it\n")
                try {
                    val osr = OutputStreamWriter(
                        requireActivity().openFileOutput(flist[i], Context.MODE_APPEND)
                    )
                    val bW = BufferedWriter(osr)
                    bW.write("Another Line")
                    bW.newLine()
                    bW.flush()
                    bW.close()
                    osr.close()

                    /* older method, commented out, but left as an example.
                    var dos = DataOutputStream(
                        requireActivity().openFileOutput(flist[i], Context.MODE_APPEND)
                    )
                    dos.writeUTF("Another line")
                    dos.close()
                    */
                } catch (e: IOException) { //which includes the FileNotFoundException e
                    e.printStackTrace()
                }

                //now read the file
                try {
                    val isr = InputStreamReader(requireActivity().openFileInput(flist[i]))
                    val dis = BufferedReader(isr)
                    line = dis.readLine()
                    while (line != null) {
                        binding.loggerlp.append(line + "\n")
                        line = dis.readLine()
                    }
                    dis.close()
                    isr.close()

                    /* older method, commented out, but left as an example.
                    val dis = DataInputStream(requireActivity().openFileInput(flist[i]))
                    while (true)
                      try {
                        binding.loggerlp.append(dis.readUTF() + "\n")
                      } catch (e: EOFException) {  //reach end of file
                        dis.close()
                      }
                      */
                } catch (e: FileNotFoundException) {
                    Log.d(TAG, "file not found: " + e.message)
                } catch (e: IOException) { //which includes the FileNotFoundException e
                    e.printStackTrace()
                }
            }
        }
    }
}