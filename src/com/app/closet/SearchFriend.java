package com.app.closet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
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
	private Button				_btnSearch;
	private EditText			_etSearchBar;
	private ParseUser			_parseUser;
	private ListView			_lvShowFriends;
	private List<String>		_listFriends	= new ArrayList<String>();
	private List<String>		_listStoreID	= new ArrayList<String>();
	private List<Bitmap>		_listImages		= new ArrayList<Bitmap>();
	private searchFriendAdapter	_friendAdapter;
	ProgressDialog				_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friend);

		_lvShowFriends = (ListView) findViewById(R.id.lvShowFriends);
		_btnSearch = (Button) findViewById(R.id.btnSearchIcon);
		_etSearchBar = (EditText) findViewById(R.id.etSearchFriend);
		_parseUser = ParseUser.getCurrentUser();

		_friendAdapter = new searchFriendAdapter(SearchFriend.this, _listFriends, _listImages);
		_lvShowFriends.setAdapter(_friendAdapter);

		_btnSearch.setOnClickListener(new SearchForFriends());

		_lvShowFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				JSONObject jObject = new JSONObject();
				JSONArray jArrayData = new JSONArray();
				try {
					ParseFile file = (ParseFile) _parseUser.getParseFile(UserData.PROFILE_PICTURE_KEY);
					String url = file.getUrl();

					jArrayData.put(Types.Username.ordinal(), _parseUser.getUsername());
					jArrayData.put(Types.Name.ordinal(), _parseUser.getString(UserData.NAME_KEY));
					jArrayData.put(Types.ObjectId.ordinal(), _parseUser.getObjectId());
					jArrayData.put(Types.ProfilePictureURL.ordinal(), URLEncoder.encode(url, "UTF-8"));

					jObject.put("alert", _parseUser.get(UserData.NAME_KEY) + " wants to add you as friend");
					jObject.put("data", jArrayData);
					jObject.put("action", "MyAction");

					ParsePush push = new ParsePush();
					push.setData(jObject);
					push.setChannel("user_" + _listStoreID.get(position));

					push.sendInBackground(new SendCallback() {
						@Override
						public void done(ParseException parseException) {
							if (parseException != null) {}// Something wrong
						}
					});

				} catch (JSONException jsonException) {
					jsonException.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private final class SearchForFriends implements OnClickListener {
		@Override
		public void onClick(View v) {
			_listFriends.clear();
			_listStoreID.clear();
			_listImages.clear();

			String getSearchType = isEmailOrName(_etSearchBar.getText().toString());
			String searchContent = _etSearchBar.getText().toString().toLowerCase(Locale.US);

			if (!searchContent.isEmpty() && searchContent != null) {
				_dialog = ProgressDialog.show(SearchFriend.this, "", LoginActivity.DIALOG_MESSAGE);

				if (getSearchType.equals(Types.Name.name())) {
					ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery(ParseUser.getCurrentUser().getClassName());
					nameQuery.whereMatches(UserData.SECONDARY_NAME_KEY, searchContent);
					nameQuery.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if (e == null) {
								if (objects.size() == 0 || (objects.get(0).getObjectId().equals(_parseUser.getObjectId()))) {
									showNoResultFound();
								}
								else {
									for (int x = 0; x < objects.size(); x++) {
										if (!(objects.get(x).getObjectId().equals(_parseUser.getObjectId()))) {
											profileInformation(objects.get(x), UserData.NAME_KEY);
										}
									}
								}

								if (_dialog != null && _dialog.isShowing())
									_dialog.dismiss();
							}
							else {
								Log.d("Search name", "The name request failed.");
							}
						}
					});
				}
				else if (getSearchType.equals(Types.Email.name())) {
					ParseQuery<ParseObject> emailQuery = ParseQuery.getQuery(ParseUser.getCurrentUser().getClassName());
					emailQuery.whereEqualTo(UserData.EMAIL_KEY, searchContent);
					try {
						if (emailQuery.count() == 0) {
							showNoResultFound();
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					emailQuery.getFirstInBackground(new GetCallback<ParseObject>() {
						@Override
						public void done(ParseObject object, ParseException e) {
							if (object != null) {
								if ((object.getObjectId().equals(_parseUser.getObjectId()))) {
										showNoResultFound();
								}
								else if (!(object.getObjectId().equals(_parseUser.getObjectId()))) {
									profileInformation(object, UserData.EMAIL_KEY);
								}
								if (_dialog != null && _dialog.isShowing())
									_dialog.dismiss();
							}
							else
								Log.d("Search Email", "The email request Failed");
						}
					});
				}

			}
		}
	}
	private void showNoResultFound () {
		DoAlertDialog alertDialog = new DoAlertDialog(SearchFriend.this);
		alertDialog.createNeutralAlertDialog("Search Friend", "No friends found");
		_friendAdapter.notifyDataSetChanged();	
	}
	private String isEmailOrName(String content) {
		if (content.contains("@"))
			return Types.Email.name();
		else
			return Types.Name.name();
	}

	private void profileInformation(final ParseObject object, final String key) {
		ParseFile file = (ParseFile) object.getParseFile(UserData.PROFILE_PICTURE_KEY);

		file.getDataInBackground(new GetDataCallback() {

			@Override
			public void done(byte[] data, ParseException e) {
				if (e == null) {
					Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
					_listImages.add(image);
					_listFriends.add(object.getString(key));
					_listStoreID.add(object.getObjectId());

					_friendAdapter.notifyDataSetChanged();
				}
			}
		});
	}
}
