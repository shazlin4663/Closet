package com.example.slidemenu;

import android.app.Activity;
import android.content.Context;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LeftScreenMenu extends SlidingMenu {

	public LeftScreenMenu(Context context, Activity activity) {
		super(context);
	
		setMode(SlidingMenu.LEFT);
		setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setShadowWidthRes(R.dimen.shadow_width);
		setShadowDrawable(R.drawable.shadow);
		setBehindOffsetRes(R.dimen.slidingmenu_offset);
		setFadeDegree(0.35f);
		attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		setMenu(R.layout.activity_left_menu);
		
	}
	

}
