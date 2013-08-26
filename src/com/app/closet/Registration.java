package com.app.closet;

import java.util.Locale;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends Activity {
	private ParseUser	_parseUser;
	private Button		_btnRegistration;
	private EditText	_etName, _etEmail, _etUsername, _etPassword;
	private TextView	_tvError;
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
		_btnRegistration = (Button) findViewById(R.id.btnRegisterSignUp);

		_btnRegistration.setOnClickListener(new RegistrationListener());
	}
		
		
	private final class RegistrationListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String username = _etUsername.getText().toString();
			String password = _etPassword.getText().toString();
			final String name = _etName.getText().toString();
			final String email = _etEmail.getText().toString();

			if (username != null && password != null && name != null && email != null && !username.isEmpty()
					&& !password.isEmpty() && !name.isEmpty() && !email.isEmpty()) {

				ParseUser user = new ParseUser();
				user.setUsername(username);
				user.setPassword(password);
				user.setEmail(email);
				
				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						if (e == null) {
							startActivity(new Intent(Registration.this, MainActivity.class));

							_etUsername.setText("");
							_etPassword.setText("");
							_etName.setText("");
							_etEmail.setText("");

							ParseObject closet = new ParseObject("Closet");
							ParseObject friend = new ParseObject("Friends");
							_parseUser = ParseUser.getCurrentUser();
							_parseUser.put("Name", name);
							_parseUser.put("SecondaryName", name.toLowerCase(Locale.US));
							_parseUser.put("Closet", closet);
							_parseUser.put("friends", friend);
							_parseUser.saveInBackground();
							
							_tvError.setVisibility(TextView.GONE);
							finish();
						}
						else {
							if (!email.contains("@"))
								_etEmail.setError("Enter correct Email");
							else
								_etUsername.setError("Username has been used");
						
							_tvError.setVisibility(TextView.GONE);
						}
					}
				});
			}
			else
				_tvError.setVisibility(TextView.VISIBLE);
		}
	}
	
	private boolean validateEmailAndUsername (String email, String username) {
	//	ParseQuery<ParseObject> 
		return  true;
	}
		
}
