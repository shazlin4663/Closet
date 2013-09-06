package com.app.closet;

import java.util.Locale;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Registration extends Activity {
	private ParseUser	_parseUser;
	private Button		_btnRegistration;
	private EditText	_etName, _etEmail, _etUsername, _etPassword;
	private TextView	_tvError;
	private ProgressDialog _progressDialog;
	private ImageView _ivFriendIcon;
	private Bitmap _profilePicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_activity_layout);

		_etName = (EditText) findViewById(R.id.etRegisterName);
		_etEmail = (EditText) findViewById(R.id.etRegisterEmail);
		_etUsername = (EditText) findViewById(R.id.etRegisterUsername);
		_etPassword = (EditText) findViewById(R.id.etRegisterPassword);
		_tvError = (TextView) findViewById(R.id.tvError);
		_ivFriendIcon = (ImageView) findViewById(R.id.ivDefaultIcon);
		
		_parseUser = new ParseUser();

		_btnRegistration = (Button) findViewById(R.id.btnRegisterSignUp);

		_btnRegistration.setOnClickListener(new RegistrationListener());
		
		_ivFriendIcon.setOnClickListener(new FriendIconListener());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
			_profilePicture = data.getParcelableExtra("image");
			_ivFriendIcon.setImageBitmap(_profilePicture);
		}
	}

	private class FriendIconListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Registration.this, CropActivity.class);
			Bundle extras = new Bundle();
			extras.putBoolean("ProfileImage", true);
			intent.putExtras(extras);
			startActivityForResult(intent, 102);
		}
		
	}

	private final class RegistrationListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final String username = _etUsername.getText().toString();
			String password = _etPassword.getText().toString();
			final String name = _etName.getText().toString();
			final String email = _etEmail.getText().toString();

			if (username != null && password != null && name != null && email != null && !username.isEmpty()
					&& !password.isEmpty() && !name.isEmpty() && !email.isEmpty()) {
				_progressDialog = ProgressDialog.show(Registration.this, "", LoginActivity.DIALOG_MESSAGE);
				_parseUser.setUsername(username);
				_parseUser.setPassword(password);
				_parseUser.setEmail(email);

				_parseUser.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							startActivity(new Intent(Registration.this, MainActivity.class));

							_etUsername.setText("");
							_etPassword.setText("");
							_etName.setText("");
							_etEmail.setText("");
							
							_parseUser = ParseUser.getCurrentUser();
							ParseObject closet = new ParseObject("Closet");
							ParseObject friend = new ParseObject("Friends");
							
							if (_profilePicture == null) {
								BitmapDrawable drawable =(BitmapDrawable) getResources().getDrawable(R.drawable.default_friend_icon); 
								_profilePicture = drawable.getBitmap();
							}
							if (_profilePicture != null) {
								ParseFile parseFile = new ParseFile("profileImage.png", MainActivity.convertBitmapToByteArray(_profilePicture));
								parseFile.saveInBackground();
								_parseUser.put("ProfilePicture", parseFile);
							}
							
							_parseUser.put("Name", name);
							_parseUser.put("SecondaryName", name.toLowerCase(Locale.US));
							_parseUser.put("Closet", closet);
							_parseUser.put("friends", friend);
							_parseUser.saveInBackground();

							_tvError.setVisibility(TextView.GONE);
							
							if (_progressDialog.isShowing() & _progressDialog != null)
								_progressDialog.dismiss();
							finish();
						}
						else {
							if (!email.contains("@"))
								_etEmail.setError("Invalid Email");
							else {
								validateEmailAndUsername("email", email);

								validateEmailAndUsername("username", username);
							}
						
							_tvError.setVisibility(TextView.GONE);
						}
						if (_progressDialog.isShowing() & _progressDialog != null)
							_progressDialog.dismiss();
					}
				});
			}
			else {

				_tvError.setVisibility(TextView.VISIBLE);
			}
		}
	}

	private void validateEmailAndUsername(String key, String query) {
		final String tempKey = key;
		ParseQuery<ParseObject> queryEmail = ParseQuery.getQuery(_parseUser.getClassName());
		queryEmail.whereEqualTo(tempKey, query);
		queryEmail.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					if (tempKey.equals("email"))
						_etEmail.setError("Email has been used");
					else
						_etUsername.setError("Username has been used");
				}
			}
		});
	}
}
