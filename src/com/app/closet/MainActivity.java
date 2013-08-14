package com.app.closet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends SlidingActivity {
	private List<String> list = new ArrayList<String>();
	private ListView lvView;
	private ItemsAdapter itemAdapter;
	private SlidingMenu menu;
	static Uri[] bitmap = new Uri[3];
	static int count = 0;
	private LinearLayout gallery, gallery1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.activity_left_menu);

		getSupportActionBar().setDisplayUseLogoEnabled(false); 
		getSupportActionBar().setDisplayShowHomeEnabled(false);
	
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View vCloth = inflater.inflate(R.layout.cloth_view, null);
		gallery1 = (LinearLayout) vCloth.findViewById(R.id.mygallery1);
		addContentView(vCloth, new LayoutParams(1000, 1000));
				
		LayoutInflater inflater2 = LayoutInflater.from(MainActivity.this);
		View vPant = inflater2.inflate(R.layout.pants_view, null);
		gallery = (LinearLayout) vPant.findViewById(R.id.mygallery);

		addContentView(vPant, new LayoutParams(2000, 2000));
		
		
		list.add("eric");
		list.add("lin");
		lvView = (ListView) findViewById(R.id.lvFriends);
		itemAdapter = new ItemsAdapter(this, list);
		lvView.setAdapter(itemAdapter);
		
		slideMenu();
	}

	private void slideMenu() {
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setBackgroundColor(getResources().getColor(R.color.gray));
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
		switch (item.getItemId()) {
		case R.id.friend:
			menu.toggle();
			break;
		case R.id.image:
			addImage();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select picture"),
				100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			String result = data.toUri(0);

			ImageView ivImage = new ImageView(getApplicationContext());
			ivImage.setLayoutParams(new LayoutParams(220, 220));
		    ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		    
		    try {
				Bitmap map = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(result)));
			    Bitmap newImage = Bitmap.createScaledBitmap(map, map.getWidth()/2, map.getHeight()/2, true);
				ivImage.setImageBitmap(newImage);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			gallery.addView(ivImage);
	//		gallery1.addView(ivImage);
		}

	}

}
