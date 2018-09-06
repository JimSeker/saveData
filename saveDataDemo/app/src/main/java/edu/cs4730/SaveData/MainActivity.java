package edu.cs4730.SaveData;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {
    int b1 = 0, b2 = 0, b3 = 0;
    DataViewModel mViewModel;

    EditText t1;
    TextView logger, tv_nothing, tv_bundle, tv_preference, tv_modelview;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        logger = (TextView) findViewById(R.id.log);
        t1 = (EditText) findViewById(R.id.editText1);

        tv_nothing = (TextView) findViewById(R.id.tv_nothing);
        tv_nothing.setText(String.valueOf(b1));

        //bundle method
        tv_bundle = (TextView) findViewById(R.id.tv_bundle);
        if (savedInstanceState != null) { //There is saved data
            logthis("There is data, restoring");
            b2 = savedInstanceState.getInt("b2", 0);  //default in case of issues.
            tv_bundle.setText(String.valueOf(b2));
        } else {
            logthis("No data in savedInstanceState");
        }

        //preference method
        tv_preference = (TextView) findViewById(R.id.tv_preference);
        //settext handled in getprefs();
        getprefs();

        //lastly the model view
        tv_modelview =  findViewById(R.id.tv_modelview);
        //for the model view live variable.
        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        mViewModel.getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer data) {
                logthis("Data changed, updating!");
                tv_modelview.setText(data.toString());
            }
        });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increment each number.  over rotations and starts, these numbers will be different.
                b1++;
                tv_nothing.setText(String.valueOf(b1));
                b2++;
                tv_bundle.setText(String.valueOf(b2));
                b3++;
                tv_preference.setText(String.valueOf(b3));

                mViewModel.increment();  //settext is handled by the observer.
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        logthis("OnPause called");
        // Store values between instances here
        SharedPreferences preferences = getSharedPreferences("example", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //store b3 in preferences
        editor.putInt("b3", b3);
        editor.commit();
        logthis("Stored preferences");
    }

    @Override
    public void onResume() {
        super.onResume();
        logthis("OnResume called");
        getprefs();
    }

    /*
     * getpres() allows me to get the sharePreferences code in on place, it is called from 
     * onCreate and onPause.
     */
    void getprefs() {
        logthis("Restoring preferences.");
        // Get the between instance stored values
        SharedPreferences preferences = getSharedPreferences("example", MODE_PRIVATE);
        //get the key d3 and set a default value of "" if the key doesn't exist.  IE the first time this app is run.
        b3 = preferences.getInt("b3", 0);
        tv_preference.setText(String.valueOf(b3));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Store the state et_bundle/b2
        savedInstanceState.putInt("b2", b2);
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
     * simple method to add the log TextView.
     */
    public void logthis(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            logger.append(newinfo + "\n");
        }
    }
}