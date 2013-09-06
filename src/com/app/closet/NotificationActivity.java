package com.app.closet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends Activity {

	private static final String					TAG					= "MyCustomReceiver";
	public static final String PREF = "ParseData";
	public final String							MESSAGE				= " wants to add you as friend";
	private ParseUser							_currentUser;
	private ProgressDialog						_dialog;
	private List<NotificationUserInformation>	_listNotification	= new ArrayList<NotificationUserInformation>();
	private ListView							_listview;
	private NotificationAdapter					_notificationAdatper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_layout);

		_currentUser = ParseUser.getCurrentUser();

		_listview = (ListView) findViewById(R.id.lvNotification);

		_notificationAdatper = new NotificationAdapter(NotificationActivity.this, _listNotification);
		_listview.setAdapter(_notificationAdatper);
	}

	/*
	 * _btnNotNow = (Button) findViewById(R.id.btnNotNow); _btnConfirm =
	 * (Button) findViewById(R.id.btnConfirm); _ivFaceImage = (ImageView)
	 * findViewById(R.id.ivImage); _tvShowText = (TextView)
	 * findViewById(R.id.tvText);
	 * 
	 * _btnConfirm.setOnClickListener(new ConfirmFriend());
	 * _btnNotNow.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * } }); }
	 * 
	 * 
	 * 
	 * private class ConfirmFriend implements OnClickListener {
	 * 
	 * @Override public void onClick(View v) { addRequestUser();
	 * addCurrentUser(); }
	 * 
	 * }
	 * 
	 * private void addRequestUser() { ParseQuery<ParseUser> query =
	 * ParseUser.getQuery(); query.whereEqualTo("objectId", _requestUserId);
	 * query.getFirstInBackground(new GetCallback<ParseUser>() {
	 * 
	 * @Override public void done(ParseUser object, ParseException e) { if (e ==
	 * null) { ParseObject userObject = object.getParseObject("friends");
	 * 
	 * try { userObject.refresh(); } catch (ParseException e1) {
	 * e1.printStackTrace(); }
	 * 
	 * final ParseObject friendList = new ParseObject("FriendID");
	 * friendList.put("friendsID", _currentUser.getObjectId());
	 * userObject.addUnique("FriendList", friendList);
	 * userObject.saveInBackground();
	 * 
	 * } else { // something went wrong }
	 * 
	 * } }); }
	 * 
	 * private void addCurrentUser() { final ParseObject currentUser =
	 * _currentUser.getParseObject("friends");
	 * 
	 * try { currentUser.refresh(); } catch (ParseException e1) {
	 * e1.printStackTrace(); }
	 * 
	 * Log.i("tag", "request: " + _requestUserId + "\n" + "currentID:" +
	 * currentUser.getObjectId());
	 * 
	 * final ParseObject friendList = new ParseObject("FriendID");
	 * friendList.put("friendsID", _requestUserId);
	 * friendList.saveInBackground(new SaveCallback() {
	 * 
	 * @Override public void done(ParseException e) {
	 * currentUser.addUnique("FriendList", friendList);
	 * 
	 * _dialog = ProgressDialog.show(NotificationActivity.this, "",
	 * LoginActivity.DIALOG_MESSAGE);
	 * 
	 * currentUser.saveInBackground(new SaveCallback() {
	 * 
	 * @Override public void done(ParseException e) { if (_dialog != null &&
	 * _dialog.isShowing()) _dialog.dismiss();
	 * 
	 * finish(); } });
	 * 
	 * } }); }
	 */

	@Override
	protected void onResume() {
		NotificationUserInformation userInfo;
	/*	if (NotificationAdapter.checkRemove == true) {
			int[] position = NotificationAdapter.getRemovePosition;
			for (int x = 0; x < position.length; x++) {
				_listNotification.remove(position[x]);
			}
			NotificationAdapter.checkRemove = false;
		}
		*/
		try {
			for (int x = 0; x < PushReceiver.dataCount; x++) {
				SharedPreferences pref = getSharedPreferences(PREF, 0);
				String[] data = pref.getString("data" + x, "").split(",");

				userInfo = new NotificationUserInformation();

				String requestUserUrl = URLDecoder.decode(data[Types.ProfilePictureURL.ordinal()], "UTF-8");
				String requestUserId = data[Types.ObjectId.ordinal()];
				String message = data[Types.Name.ordinal()] + MESSAGE;

				userInfo.setRequestUserId(requestUserId);
				userInfo.setRequestUserName(message);
				userInfo.setRequestUserUrl(requestUserUrl);
				_listNotification.add(userInfo);
			}
			_notificationAdatper.notifyDataSetChanged();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.onResume();
	}
}
