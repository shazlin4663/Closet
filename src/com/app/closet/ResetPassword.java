package com.app.closet;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends Activity {
	private Button btnSend;
	private EditText etReset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		btnSend = (Button) findViewById(R.id.btnSendEmail);
		etReset = (EditText) findViewById(R.id.etReset);
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = etReset.getText().toString();
				ParseUser.requestPasswordResetInBackground(email,
						new RequestPasswordResetCallback() {
							public void done(ParseException e) {
								if (e == null) {
									Toast toast = Toast.makeText(ResetPassword.this, "The request has been sent", Toast.LENGTH_LONG);
									toast.show();
									finish();
								} else {
									etReset.setError("Enter correct Email");
								}
							}
						});
				
			}
		});
	}
	

}
