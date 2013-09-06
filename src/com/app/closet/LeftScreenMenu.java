package com.app.closet;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class LeftScreenMenu extends LinearLayout {

	private String			_username, _email;
	private Bitmap			_profilePicture;
	private ProgressDialog	_progressDialog;
	private Activity _context;
	
	public LeftScreenMenu(Context context) {
		super(context);
	}

	public LeftScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LeftScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		_context = (Activity) getContext();
		
		Button btnNotification = (Button) findViewById(R.id.btnNotifications);
		btnNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_context, NotificationActivity.class);
				_context.startActivity(intent);
			}
		});
		
		Button btnProfile = (Button) findViewById(R.id.btnProfile);
		btnProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getUserInfoAndSaveIntoBundle();
			}
		});

		Button btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseUser.logOut();
				Activity context = (Activity) getContext();
				context.finish();
			}
		});

		Button btnAddAny = (Button) findViewById(R.id.btnCrop);
		btnAddAny.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_context, CropActivity.class);
				_context.startActivityForResult(intent, 101);
			}
		});
	}

	private void getUserInfoAndSaveIntoBundle() {
		ParseUser user = ParseUser.getCurrentUser();
		try {
			user.refresh();
			_username = user.getString(UserData.NAME_KEY);
			_email = user.getEmail();

			_progressDialog = ProgressDialog.show(getContext(), "", LoginActivity.DIALOG_MESSAGE);
			ParseFile file = (ParseFile) user.getParseFile(UserData.PROFILE_PICTURE_KEY);

			file.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(byte[] data, ParseException e) {
					if (e == null) {
						_profilePicture = BitmapFactory.decodeByteArray(data, 0, data.length);

						Intent intent = new Intent(getContext(), ProfileActivity.class);
						Bundle extra = new Bundle();
						extra.putString(UserData.NAME_KEY, _username);
						extra.putString(UserData.EMAIL_KEY, _email);
						extra.putParcelable(UserData.PROFILE_PICTURE_KEY, _profilePicture);
						intent.putExtras(extra);

						_context.startActivity(intent);
					}

					if (_progressDialog != null && _progressDialog.isShowing())
						_progressDialog.dismiss();
				}
			});

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
