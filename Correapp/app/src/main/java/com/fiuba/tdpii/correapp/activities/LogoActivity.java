package com.fiuba.tdpii.correapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.fiuba.tdpii.correapp.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LogoActivity extends AppCompatActivity {

    private static int TIME_OUT = 1700; //Time to launch the another activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logo);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent navigationIntent = new Intent(LogoActivity.this, MapHomeActivity.class);

                //TESTEO de crear
                Intent navigationIntent = new Intent(LogoActivity.this, MainActivity.class);

                startActivity(navigationIntent);
                finish();
            }
        }, TIME_OUT);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.


    }


}
