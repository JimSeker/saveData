package edu.cs4730.contentprodemo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Simple fragment to display the information from the dummy content provider.
 */
public class Contentp_Fragment extends Fragment {

    String TAG = "Contentp_frag";
    TextView output;


    public Contentp_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.contentp_fragment, container, false);

        //get the one view I need.
        output = (TextView) myView.findViewById(R.id.TextView01);


        appendthis("Query for 2 square");
        //example, select one of them, in this case 2
        Uri onerow = Uri.parse("content://edu.cs4730.provider/square/2");
        Cursor c = getActivity().getContentResolver().query(onerow, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            do {
                Log.i(TAG, "Value is" + c.getString(0));
                appendthis(c.getString(0) + " value is " + c.getString(1));
            } while (c.moveToNext());
        }

        appendthis("\nQuery all for cube:");
        //now select "all", which will return 1 to 10 cubed.
        Uri allrow = Uri.parse("content://edu.cs4730.provider/cube");
        c = getActivity().getContentResolver().query(allrow, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            do {
                Log.i(TAG, "Value is " + c.getString(0));
                appendthis(c.getString(0) + " value is " + c.getString(1));
            } while (c.moveToNext());
        }


        return myView;
    }


    public void appendthis(String item) {
        output.append("\n" + item);
    }
}
