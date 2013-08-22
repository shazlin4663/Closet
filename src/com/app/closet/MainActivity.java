package com.app.closet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends SherlockFragmentActivity {
	private SlidingMenu setUpLeftMenu, setUpRightMenu;
	private ImageView ivRightMenu, ivLeftMenu;
	private MyPagerAdapter topPageAdapter, bottomPageAdapter, shoePageAdapter,
			accessoryAdapter;
	private List<Bitmap> listTop = new ArrayList<Bitmap>();
	private List<Bitmap> listBottom = new ArrayList<Bitmap>();
	private List<Bitmap> listShoe = new ArrayList<Bitmap>();
	private List<Bitmap> listAccessory = new ArrayList<Bitmap>();
	private ParseUser parseUser;
	private static int clothCount = 0, pantCount = 0, shoeCount = 0;
	String title;
	List<ParseObject> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Parse.initialize(MainActivity.this, LoginActivity.APPLICATION_ID,
				LoginActivity.CLIENT_KEY);
		parseUser = ParseUser.getCurrentUser();

		ParseObject parseObj = parseUser.getParseObject("Closet");
		try {
			ParseObject fetch = parseObj.fetchIfNeeded();
			
			JSONArray jArrayCloset = fetch.containsKey("closet_items") ?
										fetch.getJSONArray("closet_items") :
										new JSONArray();

			for (int x = 0; x < jArrayCloset.length(); x++) {
				JSONObject closetItem;

				try {
					closetItem = jArrayCloset.getJSONObject(x);
					String closetItemID = closetItem.getString("objectId");
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("closet_item");
					query.getInBackground(closetItemID,
							new GetCallback<ParseObject>() {

								@Override
								public void done(ParseObject object,
										ParseException e) {
									if (e == null) {
										ParseFile file = object
												.getParseFile("Image");
										try {
											byte[] b = file.getData();
											Bitmap image = BitmapFactory
													.decodeByteArray(b, 0,
															b.length);
											String type = object
													.getString("Type");

											if (Types.Top.name().equals(type)) {
												listTop.add(image);
												topPageAdapter
														.notifyDataSetChanged();
											} else if (Types.Bottom.name()
													.equals(type)) {
												listBottom.add(image);
												bottomPageAdapter
														.notifyDataSetChanged();
											} else if (Types.Shoe.name()
													.equals(type)) {
												listShoe.add(image);
												shoePageAdapter
														.notifyDataSetChanged();
											} else if (Types.Accessory.name()
													.equals(type)) {
												listAccessory.add(image);
											}

										} catch (ParseException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}

								}
							});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

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
		topPageAdapter = new MyPagerAdapter(listTop, MainActivity.this);
		pagerCloth.setAdapter(topPageAdapter);

		ViewPager pagerPant = (ViewPager) findViewById(R.id.viewpagerPant);
		bottomPageAdapter = new MyPagerAdapter(listBottom, MainActivity.this);
		pagerPant.setAdapter(bottomPageAdapter);
		pagerPant.setPadding(0, 5, 0, 0);

		ViewPager pagerOther = (ViewPager) findViewById(R.id.viewpagerOther);
		shoePageAdapter = new MyPagerAdapter(listShoe, MainActivity.this);
		pagerOther.setAdapter(shoePageAdapter);
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
		ParseObject parseObject = null;
		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			String result = data.toUri(0);
			ParseFile parseFile;
			try {
				InputStream is = getContentResolver().openInputStream(
						Uri.parse(result));
				Bitmap image = CompressImage.decodeSampledBitmapFromResource(
						is, 600, 200);
				parseFile = new ParseFile("images.png",
						convertBitmapToByteArray(image));
				parseFile.saveInBackground();

				switch (LeftScreenMenu.optionImageIndex) {
				case 0:
					parseObject = parseUser.getParseObject("Closet");
					ParseObject closetItem = new ParseObject("closet_item");
					closetItem.put("Type", "Top");
					closetItem.put("Image", parseFile);
					parseObject.add("closet_items", closetItem);
					parseObject.saveInBackground();

					listTop.add(image);
					topPageAdapter.notifyDataSetChanged();
					break;
				case 1:
					parseObject = parseUser.getParseObject("PantImage");
					parseObject.put("pant" + pantCount++, parseFile);
					parseObject.saveInBackground();

					listBottom.add(image);
					bottomPageAdapter.notifyDataSetChanged();
					break;
				case 2:
					parseObject = parseUser.getParseObject("ShoeImage");
					parseObject.put("shoe" + shoeCount++, parseFile);
					parseObject.saveInBackground();

					listShoe.add(image);
					shoePageAdapter.notifyDataSetChanged();
					break;

				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, bos);
		return bos.toByteArray();
	}
}
