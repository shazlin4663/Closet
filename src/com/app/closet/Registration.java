package com.app.closet;

import java.util.Locale;

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

public class Registration extends Activity {
	private ParseUser parseUser;
	private Button btnRegistration;
	private EditText etName, etEmail, etUsername, etPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_activity_layout);
		
		etName = (EditText) findViewById(R.id.etRegisterName);
		etEmail = (EditText) findViewById(R.id.etRegisterEmail);
		etUsername = (EditText) findViewById(R.id.etRegisterUsername);
		etPassword = (EditText) findViewById(R.id.etRegisterPassword);
		btnRegistration = (Button) findViewById(R.id.btnRegisterSignUp);
		
		btnRegistration.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				final String name = etName.getText().toString();
				final String email = etEmail.getText().toString();
				
				if (username != null && password != null && name != null && email != null 
						&& !username.isEmpty() && !password.isEmpty() 
						&& !name.isEmpty() && !email.isEmpty()) {

					ParseUser user = new ParseUser();
					user.setUsername(username);
					user.setPassword(password);
					user.setEmail(email);
					user.signUpInBackground(new SignUpCallback() {

						@Override
						public void done(ParseException e) {
							if (e == null) {
								showToast("You have sign up successful.");
								startActivity(new Intent(Registration.this, MainActivity.class));
								
								etUsername.setText("");
								etPassword.setText("");
								etName.setText("");
								etEmail.setText("");
								
								ParseObject closet = new ParseObject("Closet");
								ParseObject friend = new ParseObject("Friends");
								parseUser = ParseUser.getCurrentUser();
								parseUser.put("Name", name);
								parseUser.put("SecondaryName", name.toLowerCase(Locale.US));
								parseUser.put("Closet", closet);
								parseUser.put("friends", friend);
								parseUser.saveInBackground();
								finish();
							} else 
								showToast("The username has been used.");
						}
					});
				} else 
					showToast("Please fill out all the fields");
			
			}
		});
	}
	private void showToast (String message) {
		Toast toast = Toast.makeText(Registration.this,
				message, Toast.LENGTH_LONG);
		toast.show();
	}
	

}
