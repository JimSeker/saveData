package edu.cs4730.supportprefencedemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.cs4730.supportprefencedemo.databinding.FragmentMainBinding;

/**
 * Main fragment to do most of the work.
 */
public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    FragmentMainBinding binding;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);

        //either will have the mainactivity change the fragment to a preferencefragmentcompat.
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(1);
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(2);
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getPrefs();
    }

    /*
     * Get the preferences for the game
     */
    void getPrefs() {
        boolean useSensor, useSwipe;
        //Toast.makeText(getApplicationContext(), "Get prefs", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        useSensor = prefs.getBoolean("sensorPref", false);
        useSwipe = prefs.getBoolean("swipePref", true);
        Toast.makeText(requireContext(), "Sensor is " + useSensor, Toast.LENGTH_SHORT).show();
        String text = prefs.getString("textPref", "");
        Toast.makeText(requireContext(), "Text is  " + text, Toast.LENGTH_SHORT).show();
        String list = prefs.getString("list_preference", "");
        Toast.makeText(requireContext(), "List " + list, Toast.LENGTH_SHORT).show();
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
