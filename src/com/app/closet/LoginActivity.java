package com.app.closet;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button btnLogin, btnSignup, btnResetPassword;
	private EditText etUsername, etPassword;
	public static final String APPLICATION_ID = "oN5bb0TVbdKMa7Hxws8kHUqw5T5muZltxnFNkaRK";
	public static final String CLIENT_KEY = "519GBB9lefNbw70KZFDqFH8XR0sQAYMLEPRcQcwy";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Parse.initialize(LoginActivity.this, APPLICATION_ID, CLIENT_KEY);

		setContentView(R.layout.login_screen_layout);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignup = (Button) findViewById(R.id.btnSignup);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		btnResetPassword = (Button) findViewById(R.id.btnResetPassword);

		btnResetPassword.setOnClickListener(new ResetPasswordListener());
		btnLogin.setOnClickListener(new LoginListener());
		btnSignup.setOnClickListener(new SignupListener());
	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(LoginActivity.this, message,
				Toast.LENGTH_LONG);
		toast.show();
	}

	private final class ResetPasswordListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(LoginActivity.this, ResetPassword.class));
		}

	}

	private final class SignupListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(LoginActivity.this, Registration.class));
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
									showToast("SignIn successfully");
									startActivity(new Intent(
											LoginActivity.this,
											MainActivity.class));
									etUsername.setText("");
									etPassword.setText("");
								} else
									showToast("Failed Login-Enter correct username and password");
							}
						});
			} else {
				if (username.isEmpty())
					etUsername.setError("Enter username");
				else if (password.isEmpty())
					etPassword.setError("Enter password");
				else {
					etUsername.setError("Enter username");
					etPassword.setError("Enter password");
				}
			}
		}
	}
}
