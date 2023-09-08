package edu.cs4730.contentprodemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.cs4730.contentprodemo.databinding.ContentpFragmentBinding;

/**
 * Simple fragment to display the information from the dummy content provider.
 */
public class Contentp_Fragment extends Fragment {
    String TAG = "Contentp_frag";
    ContentpFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ContentpFragmentBinding.inflate(inflater, container, false);

        appendthis("Query for 2 square");
        //example, select one of them, in this case 2
        Uri onerow = Uri.parse("content://edu.cs4730.provider/square/2");
        Cursor c = requireActivity().getContentResolver().query(onerow, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            do {
                Log.i(TAG, "Value is" + c.getString(0));
                appendthis(c.getString(0) + " value is " + c.getString(1));
            } while (c.moveToNext());
            c.close();
        }

        appendthis("\nQuery all for cube:");
        //now select "all", which will return 1 to 10 cubed.
        Uri allrow = Uri.parse("content://edu.cs4730.provider/cube");
        c = requireActivity().getContentResolver().query(allrow, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            do {
                Log.i(TAG, "Value is " + c.getString(0));
                appendthis(c.getString(0) + " value is " + c.getString(1));
            } while (c.moveToNext());
            c.close();
        }

        return binding.getRoot();
    }


    public void appendthis(String item) {
        binding.TextView01.append("\n" + item);
    }
}
