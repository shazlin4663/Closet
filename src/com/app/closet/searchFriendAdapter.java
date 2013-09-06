package com.app.closet;

import java.util.List;

import com.parse.ParseUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class searchFriendAdapter extends BaseAdapter {
	private Context _context;
	private List<String> _list;
	private List<Bitmap> _listImages;
	
	public searchFriendAdapter (Context context, List<String> list, List<Bitmap> images) {
		_context = context;
		_list = list;
		_listImages = images;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(_context);
		View itemView = layoutInflater.inflate(R.layout.items_layout_for_slidemenu, null);
		
		TextView tvUsername = (TextView) itemView.findViewById(R.id.tvshowName);
		ImageView ivImage = (ImageView) itemView.findViewById(R.id.ivFace);
		
		ivImage.setImageBitmap(_listImages.get(position));
		tvUsername.setText(_list.get(position));
		tvUsername.setShadowLayer(2, 1, 1, R.color.gray);
		return itemView;
	}

}
