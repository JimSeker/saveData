package edu.cs4730.screenshotdetectapidemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.cs4730.screenshotdetectapidemo.databinding.ActivityMainBinding;


/**
 * This example shows how to use the new screenshot detection API in Android 14.
 * It also shows how to turn on and off screenshot ability with the FLAG_SECURE flag.
 * Note, this is only available in Android 14 and higher.
 */

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean ScreenShotAllow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.fab.setOnClickListener(view -> {
            if (ScreenShotAllow) {
                // Allow screenshots and screen recordings.
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                binding.logger.append("Screenshots are allowed now.\n");
            } else {
                // Prevent screenshots and screen recordings.
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                binding.logger.append("Screenshots are NOT allowed now.\n");
            }
            ScreenShotAllow = !ScreenShotAllow;
        });

        if (ScreenShotAllow) {
            binding.logger.append("Screenshots are allowed.\n");
        } else {
            binding.logger.append("Screenshots are NOT allowed.\n");
        }

    }
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    final AppCompatActivity.ScreenCaptureCallback screenCaptureCallback =
        new Activity.ScreenCaptureCallback() {
            @Override
            public void onScreenCaptured() {
                // Add logic to take action in your app.
                binding.logger.setText("Screen captured!\n");
            }
        };


    @Override
    protected void onStart() {
        super.onStart();
        // Pass in the callback created in the previous step
        // and the intended callback executor (e.g. Activity's mainExecutor).
        registerScreenCaptureCallback( singleThreadExecutor, screenCaptureCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterScreenCaptureCallback(screenCaptureCallback);
    }
}