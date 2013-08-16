package com.app.closet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	
	public static final ImageFragment newInstance(Bitmap bitmap) {
		ImageFragment f = new ImageFragment();
		Bundle bdl = new Bundle(1);
		bdl.putParcelable(EXTRA_MESSAGE, bitmap);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Bitmap bitImage = getArguments().getParcelable(EXTRA_MESSAGE);
		View view = inflater.inflate(R.layout.imageview_layout_for_viewpager, container, false);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.tvImage);
		imageView.setImageBitmap(bitImage);
		
		return view;
	}
}
