package com.example.slidemenu;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;

public class MainActivity extends SlidingActivity {

	private Button btnLeft, btnRight;
//	SlidingMenu menu ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    setBehindContentView(R.layout.activity_main);
	    setContentView(R.layout.activity_main);

	    getSupportActionBar().setDisplayShowTitleEnabled(false);
	    getSupportActionBar().setDisplayUseLogoEnabled(false);
	    getSupportActionBar().setDisplayShowHomeEnabled(false);
	    
	 //   setSlidingActionBarEnabled(true);
//	    SlidingMenu menu = getSlidingMenu();
//	    
//	    menu.setMode(SlidingMenu.LEFT);
//	    menu.setSlidingEnabled(false);
//	    menu.setBehindOffset(100);
//
//	//    getSupportActionBar().setCustomView(R.layout.action_bar_layout);
//	    getSupportActionBar().setDisplayShowCustomEnabled(true);
//	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//	    
	    btnLeft = (Button) findViewById(R.id.btnSlideLeft);
	    btnRight = (Button) findViewById(R.id.btnSlideRight);

	    btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    new LeftScreenMenu(MainActivity.this, MainActivity.this).toggle();
			}
		});
	    
	    btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    new RightScreenMenu(MainActivity.this, MainActivity.this).toggle();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case 1:
			Log.i("openti", "sfa");
		}
		return super.onOptionsItemSelected(item);
	}
	

}
