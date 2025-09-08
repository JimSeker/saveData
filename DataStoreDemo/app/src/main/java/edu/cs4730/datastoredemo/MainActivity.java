package edu.cs4730.datastoredemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import edu.cs4730.datastoredemo.databinding.ActivityMainBinding;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * This is an example of the newer and more complex DataStore methods to replace the sharepreferences
 * Honestly, until they make you, stay with sharedpreferences, simple and easier to use.  this is neither
 * <p>
 * The kotlin and java version do the same thing, but do not match.  The kotlin version uses things that
 * not available to java, so instead we use the rxjava3 to get the flowable stuff to work correctly,
 * seriously changes the way the code looks.  but still provide the same functionality.
 * <p>
 * the kotlin version is simpler to understand
 */

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RxDataStore<Preferences> dataStore;
    Preferences.Key<Integer> DS = PreferencesKeys.intKey("DS");
    int nothing = 0;
    int myDataStore = 0;
    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        //create the data store to store the data.
        dataStore = new RxPreferenceDataStoreBuilder(this, /*name=*/ "settings").build();

        binding.button.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nothing++;
                    binding.tvNothing.setText(String.valueOf(nothing));
                    // binding.tvNothing.setText("hi");
                    myDataStore++;
                    binding.tvDatastore.setText(String.valueOf(myDataStore));
                }
            }

        );

    }

    /**
     * this function loads the information out of the data share.  We using an integer, based on the key.
     */
    public void loadDataStore() {

        Flowable<Integer> exampleCounterFlow =
            dataStore.data().map(prefs -> prefs.get(DS));
        exampleCounterFlow.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                logthis("onNext called in subscribe");
                myDataStore = integer;
                binding.tvDatastore.setText(String.valueOf(myDataStore));
            }

            @Override
            public void onError(Throwable t) {
                logthis("onError Called in subscribe");
            }

            @Override
            public void onComplete() {
                logthis("complete in subscribe");
            }
        });

    }

    /**
     * this function stores the data.  We are using a integer, based the key DS declared above.
     */
    public void saveDataStore() {
        Single<Preferences> updateResult =  //is this variable needed or will it run?
            dataStore.updateDataAsync(prefsIn -> {
                MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                //Integer currentInt = prefsIn.get(DS);  if you need to get the preferences first, use this.
                mutablePreferences.set(DS, myDataStore);
                return Single.just(mutablePreferences);
            });
        updateResult.subscribe(new SingleObserver<Preferences>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                logthis("onSubscribe in update");
            }

            @Override
            public void onSuccess(@NonNull Preferences preferences) {
                logthis("onSuccess in update");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                logthis("onError in update");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        logthis("OnResume Called");
        loadDataStore();
    }

    @Override
    public void onPause() {
        super.onPause();
        logthis("OnPause Called");
        saveDataStore();
    }

    /**
     * simple method to add the log TextView.
     */
    public void logthis(String newinfo) {
        if (newinfo.compareTo("") != 0) {
            binding.log.append(newinfo + "\n");
            Log.d(TAG, newinfo);
        }
    }

}