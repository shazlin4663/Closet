package com.app.closet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class SearchFriend extends Activity {
	private Button			btnSearch;
	private EditText		etSearchBar;
	private ParseUser		parseUser;
	private ListView		lvShowFriends;
	private List<String>	listFriends	= new ArrayList<String>();
	private List<String>	listStoreID	= new ArrayList<String>();
	private searchFriendAdapter	friendAdapter;
	ProgressDialog			dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friend);

		lvShowFriends = (ListView) findViewById(R.id.lvShowFriends);
		btnSearch = (Button) findViewById(R.id.btnSearchIcon);
		etSearchBar = (EditText) findViewById(R.id.etSearchFriend);
		parseUser = ParseUser.getCurrentUser();

		friendAdapter = new searchFriendAdapter(SearchFriend.this, listFriends);
		lvShowFriends.setAdapter(friendAdapter);

		btnSearch.setOnClickListener(new SearchForFriends());

		lvShowFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				final ParseObject friendObject = parseUser.getParseObject("friends");
				try {
					friendObject.refresh();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				final ParseObject friendList = new ParseObject("FriendID");
				friendList.put("friendsID", listStoreID.get(position));
				friendList.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						friendObject.addUnique("FriendList", friendList);

						dialog = ProgressDialog.show(SearchFriend.this, "Saving Friend", "Loading");

						friendObject.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								if (dialog != null && dialog.isShowing())
									dialog.dismiss();

								finish();
							}
						});

					}
				});

			}
		});
	}

	private final class SearchForFriends implements OnClickListener {
		@Override
		public void onClick(View v) {
			listFriends.clear();
			listStoreID.clear();

			String getSearchType = isEmailOrName(etSearchBar.getText().toString());
			String searchContent = etSearchBar.getText().toString().toLowerCase(Locale.US);

			if (!searchContent.isEmpty() && searchContent != null) {
				if (getSearchType.equals(Types.Name.name())) {
					ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery(ParseUser.getCurrentUser().getClassName());
					nameQuery.whereMatches("SecondaryName", searchContent);
					nameQuery.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if (e == null) {
								for (int x = 0; x < objects.size(); x++) {
									if (!(objects.get(x).getObjectId().equals(parseUser.getObjectId()))) {
										listFriends.add(objects.get(x).getString("Name"));
										listStoreID.add(objects.get(x).getObjectId());
									}
								}
								friendAdapter.notifyDataSetChanged();
							}
							else {
								Log.d("Search name", "The name request failed.");
							}

						}
					});
				}
				else if (getSearchType.equals(Types.Email.name())) {
					ParseQuery<ParseObject> emailQuery = ParseQuery.getQuery(ParseUser.getCurrentUser().getClassName());
					emailQuery.whereEqualTo("email", searchContent);
					emailQuery.getFirstInBackground(new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							if (object != null) {
								if (!(object.getObjectId().equals(parseUser.getObjectId()))) {
									listFriends.add(object.getString("email"));
									listStoreID.add(object.getObjectId());
									friendAdapter.notifyDataSetChanged();
								}
							}
							else
								Log.d("Search Email", "The email request Failed");
						}
					});
				}
			}
		}
	}

	private String isEmailOrName(String content) {
		if (content.contains("@"))
			return Types.Email.name();
		else
			return Types.Name.name();
	}

}
