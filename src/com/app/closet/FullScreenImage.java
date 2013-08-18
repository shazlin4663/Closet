package com.app.closet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FullScreenImage extends Activity {
	ImageView ivImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		Bitmap map = (Bitmap) extras.getParcelable("FullImage");
		
		setContentView(R.layout.full_image_layout);
		ivImage = (ImageView) findViewById(R.id.ivFullImage);
		ivImage.setImageBitmap(map);
		
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
