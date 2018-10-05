package edu.cs4730.supportprefencedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Main fragment to do most of the work.
 */
public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_main, container, false);

        /*
         * Note there are two ways to load the preference fragment.  Either via the fragment or by
         * launching another activity that displays it.  For the purposes fo the demo, I'm show both
         * methods.  Likely you would want to pick one.
         */

        //First method, an new activity to show the fragment.
        myView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), myPreferenceActivity.class));

            }
        });

        //second method, just display the new fragment.
        myView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(1);

            }
        });
        //second method, just display the new fragment.
        myView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(2);

            }
        });
        return myView;
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        useSensor = prefs.getBoolean("sensorPref", false);
        useSwipe = prefs.getBoolean("swipePref", true);
        Toast.makeText(getActivity().getApplicationContext(), "Sensor is " + useSensor, Toast.LENGTH_SHORT).show();
        String text = prefs.getString("textPref", "");
        Toast.makeText(getActivity().getApplicationContext(), "Text is  " + text, Toast.LENGTH_SHORT).show();
        String list = prefs.getString("list_preference", "");
        Toast.makeText(getActivity().getApplicationContext(), "List " + list, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
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
