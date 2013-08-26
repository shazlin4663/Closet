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
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends SherlockFragmentActivity {
	private SlidingMenu		setUpLeftMenu, setUpRightMenu;
	private ImageView		ivRightMenu, ivLeftMenu, ivAddFriend;
	private MyPagerAdapter	topPageAdapter, bottomPageAdapter, shoePageAdapter, accessoryPageAdapter;
	private List<Bitmap>	listTop			= new ArrayList<Bitmap>();
	private List<Bitmap>	listBottom		= new ArrayList<Bitmap>();
	private List<Bitmap>	listShoe		= new ArrayList<Bitmap>();
	private List<Bitmap>	listAccessory	= new ArrayList<Bitmap>();
	private ParseUser		parseUser;
	private List<ParseObject>		list;
	private RightScreenMenu rightScreenMenu;
	
	public void getUserData () {
		ParseObject parseObj = parseUser.getParseObject("Closet");
		try {
			ParseObject fetch = parseObj.fetchIfNeeded();

			JSONArray jArrayCloset = fetch.containsKey("closet_items") ? fetch.getJSONArray("closet_items")
					: new JSONArray();

			for (int x = 0; x < jArrayCloset.length(); x++) {
				JSONObject closetItem;

				try {
					closetItem = jArrayCloset.getJSONObject(x);
					String closetItemID = closetItem.getString("objectId");
					ParseQuery<ParseObject> query = ParseQuery.getQuery("closet_item");
					query.getInBackground(closetItemID, new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							if (e == null) {
								ParseFile file = object.getParseFile("Image");
								try {
									byte[] b = file.getData();
									Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
									String type = object.getString("Type");

									if (Types.Top.name().equals(type)) {
										listTop.add(image);
										topPageAdapter.notifyDataSetChanged();
									}
									else if (Types.Bottom.name().equals(type)) {
										listBottom.add(image);
										bottomPageAdapter.notifyDataSetChanged();
									}
									else if (Types.Shoe.name().equals(type)) {
										listShoe.add(image);
										shoePageAdapter.notifyDataSetChanged();
									}
									else if (Types.Accessory.name().equals(type)) {
										listAccessory.add(image);
										accessoryPageAdapter.notifyDataSetChanged();
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

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
			
		Intent intent = getIntent();
		String friendID = intent.getStringExtra("FRIEND_ID");
		if (intent.hasExtra("FRIEND_ID")) {
			ParseQuery<ParseUser> searchFriendID = ParseUser.getQuery();
			searchFriendID.whereEqualTo("objectId", friendID);
			searchFriendID.getFirstInBackground(new GetCallback<ParseUser>() {
				
				@Override
				public void done(ParseUser object, ParseException e) {
					parseUser = object;
					getUserData();
				}
			});
		}
		else {
			parseUser = ParseUser.getCurrentUser();
			getUserData();
			initalizeHomeScreen();
		}

		ViewPager pagerTop = (ViewPager) findViewById(R.id.viewPagerTop);
		topPageAdapter = new MyPagerAdapter(listTop, MainActivity.this);
		pagerTop.setAdapter(topPageAdapter);

		ViewPager pagerBottom = (ViewPager) findViewById(R.id.viewpagerBottom);
		bottomPageAdapter = new MyPagerAdapter(listBottom, MainActivity.this);
		pagerBottom.setAdapter(bottomPageAdapter);
		pagerBottom.setPadding(0, 5, 0, 0);

		ViewPager pagerShoe = (ViewPager) findViewById(R.id.viewpagerShoe);
		shoePageAdapter = new MyPagerAdapter(listShoe, MainActivity.this);
		pagerShoe.setAdapter(shoePageAdapter);
		pagerShoe.setPadding(0, 5, 0, 0);

		ViewPager pagerAccessory = (ViewPager) findViewById(R.id.viewpagerAccessory);
		accessoryPageAdapter = new MyPagerAdapter(listAccessory, MainActivity.this);
		pagerAccessory.setAdapter(accessoryPageAdapter);
		pagerAccessory.setPadding(0, 5, 0, 0);
	}
	public void initalizeHomeScreen () {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		rightScreenMenu = (RightScreenMenu) inflater.inflate(R.layout.activity_right_menu, null);
	
		createRightMenu();
		createLeftMenu();
		View customizeActionBarView = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		getSupportActionBar().setCustomView(customizeActionBarView);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		ivLeftMenu = (ImageView) customizeActionBarView.findViewById(R.id.ivLeftMenu);
		ivRightMenu = (ImageView) customizeActionBarView.findViewById(R.id.ivRightMenu);
		ivAddFriend = (ImageView) customizeActionBarView.findViewById(R.id.ivAddFriend);

		ivAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SearchFriend.class));
			}
		});

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
		setUpRightMenu.setOnOpenedListener(new OnOpenedListener() {	
			@Override
			public void onOpened() {
				rightScreenMenu.updateUI();

			}
		
		});
		
		
	}
	private void createLeftMenu() {
		setUpLeftMenu = setUpMenu();
		setUpLeftMenu.setMode(SlidingMenu.LEFT);
		setUpLeftMenu.setMenu(R.layout.activity_left_menu);
	}

	private void createRightMenu() {
		setUpRightMenu = setUpMenu();
		setUpRightMenu.setMode(SlidingMenu.RIGHT);
		setUpRightMenu.setMenu(rightScreenMenu);
		
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

		if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
			Bitmap image = data.getParcelableExtra("image");
			String type = data.getStringExtra("type");

			ParseFile parseFile = new ParseFile("images.png", convertBitmapToByteArray(image));
			parseFile.saveInBackground();

			parseObject = parseUser.getParseObject("Closet");
			ParseObject closetItem = new ParseObject("closet_item");
			closetItem.put("Type", type);
			closetItem.put("Image", parseFile);
			parseObject.add("closet_items", closetItem);
			parseObject.saveInBackground();

			if (Types.Top.name().equals(type)) {
				listTop.add(image);
				topPageAdapter.notifyDataSetChanged();
			}
			else if (Types.Bottom.name().equals(type)) {
				listBottom.add(image);
				bottomPageAdapter.notifyDataSetChanged();
			}
			else if (Types.Shoe.name().equals(type)) {
				listShoe.add(image);
				shoePageAdapter.notifyDataSetChanged();
			}
			else if (Types.Accessory.name().equals(type)) {
				listAccessory.add(image);
				accessoryPageAdapter.notifyDataSetChanged();
			}
		}
	}

	private byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, bos);
		return bos.toByteArray();
	}
}
