package com.app.closet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DoAlertDialog {
	private Context _context;
	public DoAlertDialog (Context context) {
		_context = context;
	}
	
	@SuppressLint("NewApi")
	public void createAlertDialog () {
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		
		builder.setTitle("Internet Conntection")
		.setMessage("Check you internet connection")
		.setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setCancelable(false);
		AlertDialog alert = builder.create();
		
		alert.show();
	}
}
