package com.app.closet;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter{


	private List<Fragment> _fragment;
	
	public PagerAdapter(FragmentManager fm, List<Fragment> fragment) {
		super(fm);
		_fragment = fragment;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return _fragment.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _fragment.size();
	}

}
