package com.app.closet;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends SherlockFragmentActivity {
	private SlidingMenu setUpLeftMenu, setUpRightMenu;
	private ImageView ivRightMenu, ivLeftMenu;
	private MyPagerAdapter clothPageAdapter, pantPageAdapter, otherPageAdapter;
	private List<Bitmap> listClothFragment = new ArrayList<Bitmap>();
	private List<Bitmap> listPantFragment = new ArrayList<Bitmap>();
	private List<Bitmap> listOtherFragment = new ArrayList<Bitmap>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		Parse.initialize(MainActivity.this, "closet", "app");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParseAnalytics.trackAppOpened(getIntent());
//		ParseAnalytics.trackAppOpened(getIntent());
//		ParseObject testObject = new ParseObject("TestObject");
//		testObject.put("foo", "bar");
//		testObject.saveInBackground(new SaveCallback() {
//			
//			@Override
//			public void done(ParseException e) {
//				// TODO Auto-generated method stub
//				Toast t = Toast.makeText(MainActivity.this, "saved", 2);
//				t.show();
//			}
//		});
			
		// adjust the action bar layout with one on the left and one on the
		// right
		View customizeActionBarView = LayoutInflater.from(this).inflate(
				R.layout.action_bar_layout, null);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		getSupportActionBar().setCustomView(customizeActionBarView);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		ivLeftMenu = (ImageView) customizeActionBarView
				.findViewById(R.id.ivLeftMenu);
		ivRightMenu = (ImageView) customizeActionBarView
				.findViewById(R.id.ivRightMenu);
		ivLeftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setUpLeftMenu.toggle();
			}
		});

		ivRightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setUpRightMenu.toggle();
			}
		});

		createRightMenu();
		createLeftMenu();

		ViewPager pagerCloth = (ViewPager) findViewById(R.id.viewPager);
		clothPageAdapter = new MyPagerAdapter(listClothFragment,
				MainActivity.this);
		pagerCloth.setAdapter(clothPageAdapter);

		ViewPager pagerPant = (ViewPager) findViewById(R.id.viewpagerPant);
		pantPageAdapter = new MyPagerAdapter(listPantFragment,
				MainActivity.this);
		pagerPant.setAdapter(pantPageAdapter);
		pagerPant.setPadding(0, 5, 0, 0);

		ViewPager pagerOther = (ViewPager) findViewById(R.id.viewpagerOther);
		otherPageAdapter = new MyPagerAdapter(listOtherFragment,
				MainActivity.this);
		pagerOther.setAdapter(otherPageAdapter);
		pagerOther.setPadding(0, 5, 0, 0);
		
	
	}

	private void createLeftMenu() {
		setUpLeftMenu = setUpMenu();
		setUpLeftMenu.setMode(SlidingMenu.LEFT);
		setUpLeftMenu.setMenu(R.layout.activity_left_menu);
	}

	private void createRightMenu() {
		setUpRightMenu = setUpMenu();
		setUpRightMenu.setMode(SlidingMenu.RIGHT);
		setUpRightMenu.setMenu(R.layout.activity_right_menu);
	}

	private SlidingMenu setUpMenu() {
		SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.50f);
		// menu.setBackgroundColor(getResources().getColor(R.color.aqua));
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		return menu;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			String result = data.toUri(0);
			try {
				InputStream is = getContentResolver().openInputStream(
						Uri.parse(result));
				Bitmap image = CompressImage.decodeSampledBitmapFromResource(
						is, 600, 200);

				switch (LeftScreenMenu.optionImageIndex) {
				case 0:
					listClothFragment.add(image);
					clothPageAdapter.notifyDataSetChanged();
					break;
				case 1:
					listPantFragment.add(image);
					pantPageAdapter.notifyDataSetChanged();
					break;
				case 2:
					listOtherFragment.add(image);
					otherPageAdapter.notifyDataSetChanged();
					break;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
