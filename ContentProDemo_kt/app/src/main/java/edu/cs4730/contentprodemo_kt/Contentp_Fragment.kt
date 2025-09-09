package edu.cs4730.contentprodemo_kt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.cs4730.contentprodemo_kt.databinding.ContentpFragmentBinding
import androidx.core.net.toUri

/**
 * Simple fragment to display the information from the dummy content provider.
 */
class Contentp_Fragment : Fragment() {
    var TAG: String = "Contentp_frag"
    lateinit var binding: ContentpFragmentBinding
    var provider: String = "content://edu.cs4730.provider_kt/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = ContentpFragmentBinding.inflate(inflater, container, false)

        appendthis("Query for 2 square")
        //example, select one of them, in this case 2
        val onerow = (provider + "square/2").toUri()
        var c = requireActivity().contentResolver.query(onerow, null, null, null, null)
        if (c != null) {
            c.moveToFirst()
            do {
                Log.i(TAG, "Value is" + c.getString(0))
                appendthis(c.getString(0) + " value is " + c.getString(1))
            } while (c.moveToNext())
            c.close()
        }

        appendthis("\nQuery all for cube:")
        //now select "all", which will return 1 to 10 cubed.
        val allrow = (provider + "cube").toUri()
        c = requireActivity().contentResolver.query(allrow, null, null, null, null)
        if (c != null) {
            c.moveToFirst()
            do {
                Log.i(TAG, "Value is " + c.getString(0))
                appendthis(c.getString(0) + " value is " + c.getString(1))
            } while (c.moveToNext())
            c.close()
        }

        return binding.root
    }


    fun appendthis(item: String) {
        binding.TextView01.append("\n" + item)
    }
}
