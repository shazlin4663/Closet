package com.app.closet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	
		ImageView imageView = (ImageView) view.findViewById(R.id.ivImage);
		imageView.setAdjustViewBounds(true);
		imageView.setMinimumHeight(300);
		imageView.setMinimumWidth(600);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		imageView.setImageBitmap(createImageBorder(bitImage));
		
		return view;
	}
	
	private Bitmap createImageBorder(Bitmap bitmap) {
		final int BORDER_WIDTH = 10;
	    final int BORDER_COLOR = Color.WHITE;
	    Bitmap res = Bitmap.createBitmap(bitmap.getWidth() + 2 * BORDER_WIDTH,
	                                     bitmap.getHeight() + 2 * BORDER_WIDTH,
	                                     bitmap.getConfig());
	    Canvas c = new Canvas(res);
	    Paint p = new Paint();
	    p.setColor(BORDER_COLOR);
	    c.drawRect(0, 0, res.getWidth(), res.getHeight(), p);
	    p = new Paint(Paint.FILTER_BITMAP_FLAG);
	    c.drawBitmap(bitmap, BORDER_WIDTH, BORDER_WIDTH, p);
	    return res;
	}
}
