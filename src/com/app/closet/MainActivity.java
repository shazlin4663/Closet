package com.app.closet;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends SherlockActivity {
	private SlidingMenu setUpLeftMenu, setUpRightMenu;
	private Button btnLeftMenu;
	private ImageView ivRightMenu;
	private boolean checkLeftMenu = true, checkRightMenu = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		
		View customizeActionBarView = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		getSupportActionBar().setCustomView(customizeActionBarView);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
	
		btnLeftMenu = (Button) customizeActionBarView.findViewById(R.id.btnLeftMenu);
		ivRightMenu = (ImageView) customizeActionBarView.findViewById(R.id.ivRightMenu);
		
		btnLeftMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setUpLeftMenu.toggle();		
		//		Log.i("TEst Left", " " + setUpLeftMenu.isMenuShowing());
			}
		});
		
		ivRightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					setUpRightMenu.toggle();
		//		Log.i("TEst Right", " " + setUpRightMenu.isMenuShowing());

			}
		});

		createLeftMenu();
		createRightMenu();
	}

	private void createLeftMenu() {
		setUpLeftMenu = new SlidingMenu(this);
		setUpLeftMenu.setMode(SlidingMenu.LEFT);
		setUpLeftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		setUpLeftMenu.setShadowWidthRes(R.dimen.shadow_width);
		setUpLeftMenu.setShadowDrawable(R.drawable.shadow);
		setUpLeftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		setUpLeftMenu.setFadeDegree(0.35f);
		setUpLeftMenu.setBackgroundColor(getResources().getColor(R.color.gray));
		setUpLeftMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		setUpLeftMenu.setMenu(R.layout.activity_left_menu);
	}

	private void createRightMenu() {
		setUpRightMenu = new SlidingMenu(this);
		setUpRightMenu.setMode(SlidingMenu.RIGHT);
		setUpRightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		setUpRightMenu.setShadowWidthRes(R.dimen.shadow_width);
		setUpRightMenu.setShadowDrawable(R.drawable.shadow);
		setUpRightMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		setUpRightMenu.setFadeDegree(0.35f);
		setUpRightMenu.setBackgroundColor(getResources().getColor(R.color.gray));
		setUpRightMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		setUpRightMenu.setMenu(R.layout.activity_right_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	public void addImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select picture"),
				100);
	}

	//@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			String result = data.toUri(0);
//
//			ImageView ivCloth = new ImageView(getApplicationContext());
//			ivCloth.setLayoutParams(new LayoutParams(220, 220));
//			ivCloth.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//			ImageView ivPant = new ImageView(getApplicationContext());
//			ivPant.setLayoutParams(new LayoutParams(220, 220));
//			ivPant.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//			ImageView ivOther = new ImageView(getApplicationContext());
//			ivOther.setLayoutParams(new LayoutParams(220, 220));
//			ivOther.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//			try {
//				Bitmap image = decodeSampledBitmapFromResource(
//						getContentResolver().openInputStream(Uri.parse(result)),
//						220, 220);
//		}
		}
	}

	
}
