package com.app.closet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class RightScreenMenu extends LinearLayout {

	private List<ParseUser>	list	= new ArrayList<ParseUser>();
	private List<String> 	listID = new ArrayList<String>();
	private ListView		lvView;
	private ItemsAdapter	itemAdapter;

	public RightScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RightScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RightScreenMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void updateUI() {
		updateData();
		if (itemAdapter != null)
			itemAdapter.notifyDataSetChanged();
	}

	private void updateData() {
		list.clear();
		
		ParseUser user = ParseUser.getCurrentUser();
		ParseObject getFriend = user.getParseObject("friends");
		
		try {
			ParseObject fetchFriend = getFriend.fetchIfNeeded();
			JSONArray friendArray = fetchFriend.containsKey("FriendList") ? fetchFriend.getJSONArray("FriendList")
					: new JSONArray();

			for (int x = 0; x < friendArray.length(); x++) {
				try {
					JSONObject friendID = friendArray.getJSONObject(x);
					String id = friendID.getString("objectId");

					ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendID");
					query.getInBackground(id, new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							if (e == null) {
								String userID = object.getString("friendsID");
								
								ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(ParseUser.getCurrentUser()
										.getClassName());
								innerQuery.getInBackground(userID, new GetCallback<ParseObject>() {

									@Override
									public void done(ParseObject innerobject, ParseException e) {
										if (e == null) {
											list.add((ParseUser)innerobject);
											
											itemAdapter.notifyDataSetChanged();
										}

									}
								});
							}
						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		lvView = (ListView) findViewById(R.id.lvFriend);
		itemAdapter = new ItemsAdapter(getContext(), list);
		lvView.setAdapter(itemAdapter);
		updateData();	
		
		lvView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Activity context = (Activity) getContext();
				Intent intent = new Intent(context, MainActivity.class);
				ParseUser user = list.get(arg2);
				intent.putExtra("FRIEND_ID", user.getObjectId());
				context.startActivity(intent);
				
			}});
	}
}
