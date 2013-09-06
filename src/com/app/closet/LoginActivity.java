package com.app.closet;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button btnLogin, btnSignup;
	private EditText etUsername, etPassword;
	private ParseUser parseUser;
	public static ImageData imageData = new ImageData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ParseObject.registerSubclass(ImageData.class);

		setContentView(R.layout.login_screen_layout);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignup = (Button) findViewById(R.id.btnSignup);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);

		btnLogin.setOnClickListener(new LoginListener());
		btnSignup.setOnClickListener(new SignupListener());
		}
	
	private void showToast (String message) {
		Toast toast = Toast.makeText(LoginActivity.this,
				message, Toast.LENGTH_LONG);
		toast.show();
	}
	private void createParseObject () {
		imageData.setParseCloth(new ParseObject("ClothImage"));
		imageData.setParsePant(new ParseObject("PantImage"));
		imageData.setParseShoe(new ParseObject("ShoeImage"));
		imageData.setParseAccessory(new ParseObject("AccessoryImage"));
	}
	private final class SignupListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();

			if (username != null && password != null && !username.isEmpty()
					&& !password.isEmpty()) {

				ParseUser user = new ParseUser();
				user.setUsername(username);
				user.setPassword(password);
				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							showToast("You have sign up successful.");
							startActivity(new Intent(LoginActivity.this, MainActivity.class));
							
							etUsername.setText("");
							etPassword.setText("");
							
							
//							createParseObject();
//							
							ParseObject closet = new ParseObject("Closet");
							
							parseUser = ParseUser.getCurrentUser();
							parseUser.put("Closet", closet);			
							parseUser.saveInBackground();
						} else 
							showToast("The username has been used.");
					}
				});
			} else 
				showToast("Please Enter username and password");
		}
	}

	private final class LoginListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			if (username != null && password != null && !username.isEmpty()
					&& !password.isEmpty()) {

				ParseUser.logInInBackground(username, password,
						new LogInCallback() {

							@Override
							public void done(ParseUser user, ParseException e) {
								if (user != null) { 
									showToast("SignIn successful");
									startActivity(new Intent(
											LoginActivity.this,
											MainActivity.class));
									etUsername.setText("");
									etPassword.setText("");
								} else 
									showToast("Failed Login-Enter correct username and password");
							}
						});
			} else 
				showToast("Please Enter username and password");
		}
	}
}
