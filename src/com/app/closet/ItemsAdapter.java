package com.app.closet;

import java.util.List;

import com.parse.ParseUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemsAdapter extends BaseAdapter {
	private Context _context;
	private List<ParseUser> _listName;
	
	public ItemsAdapter (Context context, List<ParseUser> list) {
		_context = context;
		_listName = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _listName.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _listName.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(_context);
		View itemView = layoutInflater.inflate(R.layout.items_layout_for_slidemenu, null);
		
		TextView tvUsername = (TextView) itemView.findViewById(R.id.tvshowName);
		ParseUser user = _listName.get(position);
		
		tvUsername.setText(user.getString("Name"));
		tvUsername.setShadowLayer(2, 1, 1, R.color.gray);
		return itemView;
	}

}
