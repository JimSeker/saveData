package edu.cs4730.supportpreference2demo_kt

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import edu.cs4730.supportpreference2demo_kt.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener { mListener!!.onFragmentInteraction(1) }

        return binding.getRoot()
    }

    override fun onResume() {
        super.onResume()
        this.prefs
    }

    val prefs: Unit
        /*
             * Get the preferences for the Activity.
             */
        get() {
            val useSensor: Boolean
            val useSwipe: Boolean
            val prefs =
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            useSensor = prefs.getBoolean("sensorPref", false)
            binding.logger.append("Sensor is $useSensor\n")
            useSwipe = prefs.getBoolean("swipePref", true)
            binding.logger.append("Swipe is $useSwipe\n")
            val text: String = prefs.getString("edittext_preference", "Not Set")!!
            binding.logger.append("Text is $text\n")
            val list: String = prefs.getString("list_preference", "Not Set")!!
            binding.logger.append("List is $list\n")
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity: Activity? = getActivity()
        try {
            mListener = activity as OnFragmentInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(which: Int)
    }
}