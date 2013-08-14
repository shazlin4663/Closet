package com.app.closet;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;

import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LeftScreenMenu {
	ListView ls;
	List<String> list = new ArrayList<String>();
	ItemsAdapter itemAdapter;
	SlidingMenu menu;
	public LeftScreenMenu(Context context, Activity activity) {
		menu= new SlidingMenu(context);
		
		menu.setContent(R.layout.activity_left_menu);
		
		list.add("sup");
		list.add("eric");
		list.add("lin");
		ls = (ListView) menu.findViewById(R.id.lvFriends);
		itemAdapter = new ItemsAdapter(menu.getContext(), list);
		
		ls.setAdapter(itemAdapter);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setMenu(R.layout.items_layout);
		menu.toggle();
	}
	

}
