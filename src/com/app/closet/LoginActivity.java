package com.app.closet;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Parse.initialize(LoginActivity.this,
				"oN5bb0TVbdKMa7Hxws8kHUqw5T5muZltxnFNkaRK",
				"519GBB9lefNbw70KZFDqFH8XR0sQAYMLEPRcQcwy");

		setContentView(R.layout.login_screen_layout);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignup = (Button) findViewById(R.id.btnSignup);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);

		btnLogin.setOnClickListener(new LoginListener());
		btnSignup.setOnClickListener(new SignupListener());
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
							Toast t = Toast.makeText(LoginActivity.this,
									"success signup", 2);
							t.show();
							startActivity(new Intent(LoginActivity.this,
									MainActivity.class));

						} else {
							Toast t = Toast.makeText(LoginActivity.this,
									"failed signup", 2);
							t.show();

						}
					}
				});
			} else {
				Toast t = Toast.makeText(LoginActivity.this,
						"Enter username and password", 2);
				t.show();
			}
			etUsername.setText("");
			etPassword.setText("");
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
								// TODO Auto-generated method stub
								if (user != null) {
									Toast t = Toast.makeText(
											LoginActivity.this,
											"success Login", 2);
									t.show();
									startActivity(new Intent(
											LoginActivity.this,
											MainActivity.class));

								} else {
									Toast t = Toast.makeText(
											LoginActivity.this, "Failed Login",
											2);
									t.show();
								}
							}
						});
			} else {
				Toast t = Toast.makeText(LoginActivity.this,
						"Eneter username and password", 2);
				t.show();

			}
		}
	}
}
