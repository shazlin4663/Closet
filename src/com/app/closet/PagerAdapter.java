package com.app.closet;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class PagerAdapter extends FragmentPagerAdapter{


	private List<Fragment> _fragment;
	
	public PagerAdapter(FragmentManager fm, List<Fragment> fragment, Context context) {
		super(fm);
		_fragment = fragment;
	}

	@Override
	public float getPageWidth(int position) {
		return 0.5f;
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return _fragment.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _fragment.size();
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}


	
}
