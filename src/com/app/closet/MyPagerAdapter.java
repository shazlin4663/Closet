package com.app.closet;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
public class MyPagerAdapter extends PagerAdapter{

	List<Bitmap> _listBitmap = new ArrayList<Bitmap>();
	Context _context;
	Bitmap bitmapImage;
	public MyPagerAdapter (List<Bitmap> listBitmap, Context context){
		_listBitmap = listBitmap;
		_context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _listBitmap.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public float getPageWidth(int position) {
		// TODO Auto-generated method stub
		return 0.5f;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		bitmapImage = _listBitmap.get(position);
		
		LayoutInflater inflater = LayoutInflater.from(_context);
		View view = inflater.inflate(R.layout.imageview_layout_for_viewpager, null);
		ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
		
		ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);		
		ivImage.setImageBitmap(createImageBorder(bitmapImage));
		
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(_context, FullScreenImage.class);
				Bundle extras = new Bundle();
				extras.putParcelable("FullImage", bitmapImage);
				intent.putExtras(extras);
				
				_context.startActivity(intent);
			}
		});
		
		container.addView(view);
		return view;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
	//	super.destroyItem(container, position, object);
		container.removeViewAt(position);
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
