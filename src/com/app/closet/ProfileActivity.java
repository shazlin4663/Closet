package com.app.closet;

import java.util.Locale;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	private TextView			_tvName, _tvEmail, _tvShowName, _tvProfileName, _tvPassword;
	private Button				_btnSubmit;
	private DoAlertDialog		_dialogBuilder;
	private String				_name, _email;
	private ProgressDialog		_progressDialog;
	private Bitmap				_profilePicture;
	private ImageView			_ivPicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_layout);

		_btnSubmit = (Button) findViewById(R.id.btnUpdateProfile);
		_tvName = (TextView) findViewById(R.id.tvEditName);
		_tvEmail = (TextView) findViewById(R.id.tvEditEmail);
		_tvPassword = (TextView) findViewById(R.id.tvEditPassword);
		_tvShowName = (TextView) findViewById(R.id.tvShowUsername);
		_tvProfileName = (TextView) findViewById(R.id.tvProfileView);
		_ivPicture = (ImageView) findViewById(R.id.ivProfilePicture);

		_dialogBuilder = new DoAlertDialog(ProfileActivity.this);

		Bundle extras = getIntent().getExtras();

		if (extras.containsKey(UserData.NAME_KEY) && extras.containsKey(UserData.EMAIL_KEY) && !extras.isEmpty()
				&& extras.containsKey(UserData.PROFILE_PICTURE_KEY)) {

			_name = extras.getString(UserData.NAME_KEY);
			_email = extras.getString(UserData.EMAIL_KEY);
			_profilePicture = extras.getParcelable(UserData.PROFILE_PICTURE_KEY);

			_tvEmail.setOnClickListener(new EmailChangeListener());
			_tvName.setOnClickListener(new NameChangeListener());
			_tvPassword.setOnClickListener(new PasswordChangeListener());
			_btnSubmit.setOnClickListener(new UpdateProfileListener());
			_ivPicture.setOnClickListener(new PictureChangeListener());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
			_profilePicture = data.getParcelableExtra("image");
		}
	}

	private class PictureChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ProfileActivity.this, CropActivity.class);
			Bundle extras = new Bundle();
			extras.putBoolean("ProfileImage", true);
			intent.putExtras(extras);
			startActivityForResult(intent, 102);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		_tvShowName.setText("I'm " + _name);
		_tvProfileName.setText(_name + "'s Profile:");
		_tvEmail.setText(_email);
		_tvName.setText(_name);
		_ivPicture.setImageBitmap(_profilePicture);

	}

	private class UpdateProfileListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			_progressDialog = ProgressDialog.show(ProfileActivity.this, "", "Saving");
			ParseUser user = ParseUser.getCurrentUser();

			if (!_email.isEmpty() && _email != null && _email.contains("@") && _email.contains(".com")) {
				user.setEmail(_email);

				ParseQuery<ParseObject> query = ParseQuery.getQuery(user.getClassName());
				query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject object, ParseException e) {
						if (e == null) {
							ParseFile file = new ParseFile("profileImage.png", MainActivity
									.convertBitmapToByteArray(_profilePicture));
							file.saveInBackground();

							object.put(UserData.PROFILE_PICTURE_KEY, file);
							object.put(UserData.NAME_KEY, _name);
							object.put(UserData.SECONDARY_NAME_KEY, _name.toLowerCase(Locale.US));
							object.saveInBackground();

							if (_progressDialog != null && _progressDialog.isShowing())
								_progressDialog.dismiss();
							finish();
						}
					}
				});
			}
			else {
				if (_progressDialog != null && _progressDialog.isShowing())
					_progressDialog.dismiss();

				Toast toast = Toast.makeText(ProfileActivity.this, "Invalid Email", Toast.LENGTH_LONG);
				toast.show();

			}
		}

	}

	private class PasswordChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(ProfileActivity.this, ResetPassword.class));
		}

	}

	private class EmailChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			_dialogBuilder = new DoAlertDialog(ProfileActivity.this);
			final AlertDialog dialog = _dialogBuilder.editEntryAlertDialog();
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialogInterface) {
					String tempEmail = _dialogBuilder.getEntry();
					if (!tempEmail.isEmpty() && tempEmail != null && tempEmail.contains("@")
							&& tempEmail.contains(".com")) {

						_email = tempEmail;
						onResume();
					}
					else if (!tempEmail.isEmpty() && tempEmail != null) {
						_email = "";
						Toast toast = Toast.makeText(ProfileActivity.this, "Invalid Email", Toast.LENGTH_LONG);
						toast.show();
					}
				}
			});
		}
	}

	private class NameChangeListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			_dialogBuilder = new DoAlertDialog(ProfileActivity.this);
			final AlertDialog dialog = _dialogBuilder.editEntryAlertDialog();
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialogInterface) {
					String tempName = _dialogBuilder.getEntry();
					if (!tempName.isEmpty() && tempName != null) {
						_name = _dialogBuilder.getEntry();
						onResume();
					}

				}
			});
		}

	}
}
