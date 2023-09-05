package edu.cs4730.supportprefencedemo_kt


import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.widget.Toast
import android.app.Activity
import android.content.Context
import androidx.preference.PreferenceManager
import android.view.View
import androidx.fragment.app.Fragment
import edu.cs4730.supportprefencedemo_kt.databinding.FragmentMainBinding
import java.lang.ClassCastException

/**
 * Main fragment to do most of the work.
 */
class MainFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)

        //either will have the mainactivity change the fragment to a preferencefragmentcompat.
        binding.button2.setOnClickListener { mListener!!.onFragmentInteraction(1) }
        binding.button3.setOnClickListener { mListener!!.onFragmentInteraction(2) }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        prefs
    }

    /*
     * Get the preferences for the game
     */
    val prefs: Unit
        get() {
            val useSensor: Boolean
            val useSwipe: Boolean
            //Toast.makeText(getApplicationContext(), "Get prefs", Toast.LENGTH_SHORT).show();
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            useSensor = prefs.getBoolean("sensorPref", false)
            useSwipe = prefs.getBoolean("swipePref", true)
            Toast.makeText(requireContext(), "Sensor is $useSensor", Toast.LENGTH_SHORT).show()
            val text = prefs.getString("textPref", "")
            Toast.makeText(requireContext(), "Text is  $text", Toast.LENGTH_SHORT).show()
            val list = prefs.getString("list_preference", "")
            Toast.makeText(requireContext(), "List $list", Toast.LENGTH_SHORT).show()
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity? = activity
        mListener = try {
            activity as OnFragmentInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString() + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(which: Int)
    }
}