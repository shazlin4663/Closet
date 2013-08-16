package com.app.closet;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class LeftScreenMenu extends LinearLayout {

	private List<String> list = new ArrayList<String>();
	private ListView lvView;
	private ItemsAdapter itemAdapter;
	
	public LeftScreenMenu(Context context) {
		super(context);
	}

	public LeftScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public LeftScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		lvView = (ListView) findViewById(R.id.lvFriends);
		list.add("Eric");
		list.add("lin");
		
		itemAdapter = new ItemsAdapter(getContext(), list);
		lvView.setAdapter(itemAdapter);

	}
	
}
