package com.app.closet;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class RightScreenMenu extends LinearLayout {

	private List<String> list = new ArrayList<String>();
	private ListView lvView;
	private ItemsAdapter itemAdapter;
	private Activity ac = new Activity();

	public RightScreenMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RightScreenMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RightScreenMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		
		lvView = (ListView) findViewById(R.id.lvOther);
		list.add("Eric");
		list.add("lin");
		
		itemAdapter = new ItemsAdapter(getContext(), list);
		lvView.setAdapter(itemAdapter);

	}	


	
}
