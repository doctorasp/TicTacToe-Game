package com.example.deepbrain.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DeepBrain on 1/3/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start home activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                // close splash activity
                finish();
            }
        }, 2000);
    }
}
