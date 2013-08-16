package com.app.closet;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends SherlockFragmentActivity {
	private int optionImageIndex;
	private SlidingMenu setUpLeftMenu, setUpRightMenu;
	private Button btnLeftMenu;
	private ImageView ivRightMenu;
	private PagerAdapter clothPageAdapter, pantPageAdapter, otherPageAdapter;
	private List<Fragment> listClothFragment = new ArrayList<Fragment>();
	private List<Fragment> listPantFragment = new ArrayList<Fragment>();
	private List<Fragment> listOtherFragment = new ArrayList<Fragment>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// adjust the action bar layout with one on the left and one on the right
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
			}
		});
		
		ivRightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					setUpRightMenu.toggle();
			}
		});

		createLeftMenu();
		createRightMenu();
		
		ViewPager pagerCloth = (ViewPager)findViewById(R.id.viewPager);
		clothPageAdapter = new PagerAdapter(getSupportFragmentManager(), listClothFragment);
		pagerCloth.setAdapter(clothPageAdapter);
	
		ViewPager pagerPant = (ViewPager)findViewById(R.id.viewpagerPant);
		pantPageAdapter = new PagerAdapter(getSupportFragmentManager(), listPantFragment);
		pagerPant.setAdapter(pantPageAdapter);

		ViewPager pagerOther = (ViewPager)findViewById(R.id.viewpagerOther);
		otherPageAdapter = new PagerAdapter(getSupportFragmentManager(), listOtherFragment);
		pagerOther.setAdapter(otherPageAdapter);
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
		switch (item.getItemId()) {
		case R.id.addImageCloth:
			addImage();
			optionImageIndex = 0;
			break;
		case R.id.addImagePant:
			addImage();
			optionImageIndex = 1;
			break;
		case R.id.addImageOther:
			addImage();
			optionImageIndex = 2;
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

	//@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			String result = data.toUri(0);
			try {
				InputStream is = getContentResolver().openInputStream(Uri.parse(result));
				Bitmap image = CompressImage.decodeSampledBitmapFromResource(is, 200, 200);
				
				switch (optionImageIndex) {
				case 0:
					listClothFragment.add(ImageFragment.newInstance(image));
					clothPageAdapter.notifyDataSetChanged();
					break;
				case 1:
					listPantFragment.add(ImageFragment.newInstance(image));
					pantPageAdapter.notifyDataSetChanged();
					break;
				case 2:
					listOtherFragment.add(ImageFragment.newInstance(image));
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
