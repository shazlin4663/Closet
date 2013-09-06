package com.app.closet;

import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class LeftScreenMenu extends LinearLayout {

	public LeftScreenMenu(Context context) {
		super(context);
	}

	public LeftScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LeftScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

		Button btnLogout = (Button) findViewById(R.id.btnLogout);
		btnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseUser.logOut();
				Activity context = (Activity) getContext();
				context.finish();
			}
		});

		Button btnAddAny = (Button) findViewById(R.id.btnCrop);
		btnAddAny.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity context = (Activity) getContext();

				Intent intent = new Intent(context, CropActivity.class);
				context.startActivityForResult(intent, 101);
			}
		});
	}
}
