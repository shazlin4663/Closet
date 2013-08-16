package com.app.closet;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class DisplayImage extends RelativeLayout {

	FragmentActivity fragmentActivity;
	PagerAdapter pageAdapter;
	public DisplayImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DisplayImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public DisplayImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		fragmentActivity = new FragmentActivity();
		List<Fragment> fragments = getFragments();
		pageAdapter = new PagerAdapter(fragmentActivity.getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);	
	}
	
	private List<Fragment> getFragments(){
		List<Fragment> fList = new ArrayList<Fragment>();
		 
		fList.add(MyFragment.newInstance("Fragment 1"));
		fList.add(MyFragment.newInstance("Fragment 2"));
		fList.add(MyFragment.newInstance("Fragment 3"));
		return fList;
	}
}