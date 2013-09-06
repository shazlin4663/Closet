package com.app.closet;

import com.app.closet.util.SystemUiHider;
import com.parse.Parse;
import com.parse.ParseUser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class SplashScreenActivity extends Activity {
	
	public static final String APPLICATION_ID = "oN5bb0TVbdKMa7Hxws8kHUqw5T5muZltxnFNkaRK";
	public static final String CLIENT_KEY = "519GBB9lefNbw70KZFDqFH8XR0sQAYMLEPRcQcwy";
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Parse.initialize(SplashScreenActivity.this, APPLICATION_ID, CLIENT_KEY);
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	ParseUser currentUser = ParseUser.getCurrentUser();
            	if (currentUser != null) {
            		Toast toast = Toast.makeText(SplashScreenActivity.this,
            				"SignIn successful", Toast.LENGTH_LONG);
            		toast.show();
					startActivity(new Intent(
							SplashScreenActivity.this,
							MainActivity.class));
            	} else {
            		Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
            	} 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}
