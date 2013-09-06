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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

public class MainActivity extends SherlockFragmentActivity {
	private SlidingMenu			_setUpLeftMenu, _setUpRightMenu;
	private ImageView			_ivRightMenu, _ivLeftMenu, _ivAddFriend;
	private MyPagerAdapter		_topPageAdapter, _bottomPageAdapter, _shoePageAdapter, _accessoryPageAdapter;
	private List<Bitmap>		_listTop		= new ArrayList<Bitmap>();
	private List<Bitmap>		_listBottom		= new ArrayList<Bitmap>();
	private List<Bitmap>		_listShoe		= new ArrayList<Bitmap>();
	private List<Bitmap>		_listAccessory	= new ArrayList<Bitmap>();
	private ParseUser			_parseUser;
	private RightScreenMenu		_rightScreenMenu;
	public static final String	INTENT_KEY		= "FRIEND_ID";
	private UserData		_userInfo;
	private ProgressDialog		_progressDialog;

	public void getUserData() {
		ParseObject parseObj = _parseUser.getParseObject(UserData.OBJECT_CLOSET_KEY);
		try {
			ParseObject fetch = parseObj.fetchIfNeeded();

			JSONArray jArrayCloset = fetch.containsKey("closet_items") ? fetch.getJSONArray("closet_items")
					: new JSONArray();

			for (int x = 0; x < jArrayCloset.length(); x++) {
				JSONObject closetItem;

				try {
					closetItem = jArrayCloset.getJSONObject(x);
					String closetItemID = closetItem.getString(UserData.USER_ID_KEY);
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
										_listTop.add(image);
										_topPageAdapter.notifyDataSetChanged();
									}
									else if (Types.Bottom.name().equals(type)) {
										_listBottom.add(image);
										_bottomPageAdapter.notifyDataSetChanged();
									}
									else if (Types.Shoe.name().equals(type)) {
										_listShoe.add(image);
										_shoePageAdapter.notifyDataSetChanged();
									}
									else if (Types.Accessory.name().equals(type)) {
										_listAccessory.add(image);
										_accessoryPageAdapter.notifyDataSetChanged();
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
	//	_progressDialog = ProgressDialog.show(MainActivity.this, "", LoginActivity.DIALOG_MESSAGE);

		PushService.setDefaultPushCallback(this, NotificationActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		Intent intent = getIntent();
		String friendID = intent.getStringExtra(INTENT_KEY);
		if (intent.hasExtra(INTENT_KEY)) {
			ParseQuery<ParseUser> searchFriendID = ParseUser.getQuery();
			searchFriendID.whereEqualTo(UserData.USER_ID_KEY, friendID);
			searchFriendID.getFirstInBackground(new GetCallback<ParseUser>() {

				@Override
				public void done(ParseUser object, ParseException e) {
					_parseUser = object;
					getUserData();
				}
			});
		}
		else {
			_parseUser = ParseUser.getCurrentUser();
			getUserData();
			initalizeHomeScreen();
			PushService.subscribe(MainActivity.this, "user_" + _parseUser.getObjectId(), MainActivity.class);
		}
		
		ViewPager pagerTop = (ViewPager) findViewById(R.id.viewPagerTop);
		_topPageAdapter = new MyPagerAdapter(_listTop, MainActivity.this);
		pagerTop.setAdapter(_topPageAdapter);

		ViewPager pagerBottom = (ViewPager) findViewById(R.id.viewpagerBottom);
		_bottomPageAdapter = new MyPagerAdapter(_listBottom, MainActivity.this);
		pagerBottom.setAdapter(_bottomPageAdapter);
		pagerBottom.setPadding(0, 5, 0, 0);

		ViewPager pagerShoe = (ViewPager) findViewById(R.id.viewpagerShoe);
		_shoePageAdapter = new MyPagerAdapter(_listShoe, MainActivity.this);
		pagerShoe.setAdapter(_shoePageAdapter);
		pagerShoe.setPadding(0, 5, 0, 0);

		ViewPager pagerAccessory = (ViewPager) findViewById(R.id.viewpagerAccessory);
		_accessoryPageAdapter = new MyPagerAdapter(_listAccessory, MainActivity.this);
		pagerAccessory.setAdapter(_accessoryPageAdapter);
		pagerAccessory.setPadding(0, 5, 0, 0);

/*		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				_progressDialog.dismiss();
			}
		}, 5000);*/
	}

	@Override
	protected void onResume() {
	/*	try {
			_parseUser.refresh();
			ParseQuery<ParseObject> query = ParseQuery.getQuery(_parseUser.getClassName());
			query.getInBackground(_parseUser.getObjectId(), new GetCallback<ParseObject>() {

				@Override
				public void done(ParseObject object, ParseException e) {
					if (e == null) {
						ParseFile file = (ParseFile) object.getParseFile(UserData.PROFILE_PICTURE);
						file.getDataInBackground(new GetDataCallback() {

							@Override
							public void done(byte[] data, ParseException innerE) {
								_userInfo.setImage(data);	
							}
						});
						_userInfo.setUsername(object.getString(UserData.USERNAME));
						_userInfo.setEmail(object.getString(UserData.EMAIL));
						_userInfo.setName(object.getString(UserData.NAME));
					}
				}

			});
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		super.onResume();
	}

	public void initalizeHomeScreen() {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		_rightScreenMenu = (RightScreenMenu) inflater.inflate(R.layout.activity_right_menu, null);

		createRightMenu();
		createLeftMenu();
		View customizeActionBarView = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		getSupportActionBar().setCustomView(customizeActionBarView);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		_ivLeftMenu = (ImageView) customizeActionBarView.findViewById(R.id.ivLeftMenu);
		_ivRightMenu = (ImageView) customizeActionBarView.findViewById(R.id.ivRightMenu);
		_ivAddFriend = (ImageView) customizeActionBarView.findViewById(R.id.ivAddFriend);

		_ivAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SearchFriend.class));
			}
		});

		_ivLeftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_setUpLeftMenu.toggle();
			}
		});

		_ivRightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_setUpRightMenu.toggle();
			}
		});
		_setUpRightMenu.setOnOpenedListener(new OnOpenedListener() {
			@Override
			public void onOpened() {
				_rightScreenMenu.updateUI();
			}
		});
	}

	private void createLeftMenu() {
		_setUpLeftMenu = setUpMenu();
		_setUpLeftMenu.setMode(SlidingMenu.LEFT);
		_setUpLeftMenu.setMenu(R.layout.activity_left_menu);
	}

	private void createRightMenu() {
		_setUpRightMenu = setUpMenu();
		_setUpRightMenu.setMode(SlidingMenu.RIGHT);
		_setUpRightMenu.setMenu(_rightScreenMenu);

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

		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:

				break;
		}
		return super.onOptionsItemSelected(item);
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

			parseObject = _parseUser.getParseObject(UserData.OBJECT_CLOSET_KEY);
			ParseObject closetItem = new ParseObject("closet_item");
			closetItem.put("Type", type);
			closetItem.put("Image", parseFile);
			parseObject.add("closet_items", closetItem);
			parseObject.saveInBackground();

			if (Types.Top.name().equals(type)) {
				_listTop.add(image);
				_topPageAdapter.notifyDataSetChanged();
			}
			else if (Types.Bottom.name().equals(type)) {
				_listBottom.add(image);
				_bottomPageAdapter.notifyDataSetChanged();
			}
			else if (Types.Shoe.name().equals(type)) {
				_listShoe.add(image);
				_shoePageAdapter.notifyDataSetChanged();
			}
			else if (Types.Accessory.name().equals(type)) {
				_listAccessory.add(image);
				_accessoryPageAdapter.notifyDataSetChanged();
			}
		}
	}

	public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, bos);
		return bos.toByteArray();
	}
}
