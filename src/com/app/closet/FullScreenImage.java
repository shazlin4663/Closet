package com.app.closet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class FullScreenImage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image_layout);
		ImageView ivImage = (ImageView) findViewById(R.id.ivFullImage);
		
	}

}
