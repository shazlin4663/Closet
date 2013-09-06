package com.app.closet;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private Button				_btnLogin, _btnSignup, _btnResetPassword;
	private EditText			_etUsername, _etPassword;
	public static final String	APPLICATION_ID	= "oN5bb0TVbdKMa7Hxws8kHUqw5T5muZltxnFNkaRK";
	public static final String	CLIENT_KEY		= "519GBB9lefNbw70KZFDqFH8XR0sQAYMLEPRcQcwy";
	private ProgressDialog		_progressDialog;
	private TextView			_tvShowError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Parse.initialize(LoginActivity.this, APPLICATION_ID, CLIENT_KEY);

		setContentView(R.layout.login_screen_layout);

		_btnLogin = (Button) findViewById(R.id.btnLogin);
		_btnSignup = (Button) findViewById(R.id.btnSignup);
		_etUsername = (EditText) findViewById(R.id.etUsername);
		_etPassword = (EditText) findViewById(R.id.etPassword);
		_btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
		_tvShowError = (TextView) findViewById(R.id.tvShowError);

		_btnResetPassword.setOnClickListener(new ResetPasswordListener());
		_btnLogin.setOnClickListener(new LoginListener());
		_btnSignup.setOnClickListener(new SignupListener());
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
			String username = _etUsername.getText().toString();
			String password = _etPassword.getText().toString();

			if (isNetworkAvailable()) {
				if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
					_progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading");

					ParseUser.logInInBackground(username, password, new LogInCallback() {

						@Override
						public void done(ParseUser user, ParseException e) {
							if (user != null) {
								if (_progressDialog != null && _progressDialog.isShowing())
									_progressDialog.dismiss();

								startActivity(new Intent(LoginActivity.this, MainActivity.class));
								_etUsername.setText("");
								_etPassword.setText("");
								_tvShowError.setVisibility(TextView.GONE);
							}
							else {
								_tvShowError.setVisibility(TextView.VISIBLE);
								if (_progressDialog != null && _progressDialog.isShowing())
									_progressDialog.dismiss();
							}
						}
					});
				}
				else {
					if (username.isEmpty())
						_etUsername.setError("Enter username");
					else if (password.isEmpty())
						_etPassword.setError("Enter password");
					else {
						_etUsername.setError("Enter username");
						_etPassword.setError("Enter password");
					}
				}
			}
			else {
				DoAlertDialog dialog = new DoAlertDialog(LoginActivity.this);
				dialog.createAlertDialog();
			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		else {
			// display error
			return false;
		}
	}
}
