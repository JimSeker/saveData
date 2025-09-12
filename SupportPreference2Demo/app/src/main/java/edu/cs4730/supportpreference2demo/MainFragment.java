package edu.cs4730.supportpreference2demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cs4730.supportpreference2demo.databinding.FragmentMainBinding;


public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFragmentInteraction(1);
                }
            }

        );

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPrefs();
    }

    /*
     * Get the preferences for the Activity.
     */
    void getPrefs() {
        boolean useSensor, useSwipe;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        useSensor = prefs.getBoolean("sensorPref", false);
        binding.logger.append("Sensor is " + useSensor + "\n");
        useSwipe = prefs.getBoolean("swipePref", true);
        binding.logger.append("Swipe is " + useSwipe + "\n");
        String text = prefs.getString("edittext_preference", "Not Set");
        binding.logger.append("Text is " + text + "\n");
        String list = prefs.getString("list_preference", "Not Set");
        binding.logger.append("List is " + list + "\n");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int which);
    }
}