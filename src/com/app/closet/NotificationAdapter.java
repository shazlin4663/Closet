package com.app.closet;

import java.util.ArrayList;
import java.util.List;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends BaseAdapter {
	private List<NotificationUserInformation>	_listNotification	= new ArrayList<NotificationUserInformation>();
	private Context								_context;
	private String								_id, _message, _url;
	public static int[]							getRemovePosition;
	public static boolean						checkRemove;
	public static int							count;
	private ParseUser _currentUser;
	private ProgressDialog						_dialog;
	private int _position;
	
	public NotificationAdapter(Context context, List<NotificationUserInformation> listNotification) {
		_context = context;
		_listNotification = listNotification;
		_currentUser = ParseUser.getCurrentUser();
		_position = 0;
		// checkRemove = false;
		// / getRemovePosition = new int[PushReceiver.dataCount];
		// count = 0;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _listNotification.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _listNotification.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(_context);
		View view = inflater.inflate(R.layout.notification_listview_layout, null);
		_position = position;
		
		final Button btnNotNow = (Button) view.findViewById(R.id.btnNotNow);
		final Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		final ImageView ivFaceImage = (ImageView) view.findViewById(R.id.ivImage);
		TextView tvShowText = (TextView) view.findViewById(R.id.tvText);

		_id = _listNotification.get(position).getRequestUserId();
		_message = _listNotification.get(position).getRequestUserName();
		_url = _listNotification.get(position).getRequestUserUrl();

		tvShowText.setText(_message);
		btnNotNow.setTag(position);
		btnConfirm.setTag(position);
		
		new DownloadImage() {
			@Override
			protected void onPostExecute(Bitmap result) {
				ivFaceImage.setImageBitmap(result);
				super.onPostExecute(result);
			}
		}.execute(_url);

		btnNotNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnNotNow.setVisibility(Button.GONE);
				btnConfirm.setVisibility(Button.GONE);
//				Log.i("tag", "request: " + _listNotification.get((Integer) v.getTag()).getRequestUserId() + "\n" + v.getTag());
			}
		});

		btnConfirm.setOnClickListener(new ConfirmFriend());

		return view;
	}

	private class ConfirmFriend implements OnClickListener {

		@Override
		public void onClick(View v) {
			String id = _listNotification.get((Integer) v.getTag()).getRequestUserId();
//			Log.i("tag", "request: " + _listNotification.get((Integer) v.getTag()).getRequestUserId() + "\n" + v.getTag());
			addRequestUser(id);
			addCurrentUser();
		}
	}

	private void addRequestUser(String requestId) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo(UserData.USER_ID_KEY, requestId);
		query.getFirstInBackground(new GetCallback<ParseUser>() {

			@Override
			public void done(ParseUser object, ParseException e) {
				if (e == null) {
					ParseObject userObject = object.getParseObject(UserData.OBJECT_FRIEND_KEY);

					try {
						userObject.refresh();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

					final ParseObject friendList = new ParseObject("FriendID");
					 friendList.put("friendsID", _currentUser.getObjectId());
					userObject.addUnique("FriendList", friendList);
					userObject.saveInBackground();

				}
				else { // something went wrong }

				}
			}
		});
	}

	private void addCurrentUser() {
		final ParseObject currentUser = _currentUser.getParseObject("friends");

		try {
			currentUser.refresh();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Log.i("tag", "request: " + _id + "\n" + "currentID:" + currentUser.getObjectId());

		final ParseObject friendList = new ParseObject("FriendID");
		friendList.put("friendsID", _id);
		friendList.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				currentUser.addUnique("FriendList", friendList);

				 _dialog = ProgressDialog.show(_context, "", LoginActivity.DIALOG_MESSAGE);

				currentUser.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						if (_dialog != null && _dialog.isShowing())
							_dialog.dismiss();
					}
				});

			}
		});
	}

}
