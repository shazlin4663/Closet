package com.app.closet;

import java.util.List;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemsAdapter extends BaseAdapter {
	private Context			_context;
	private List<ParseUser>	_listName;

	public ItemsAdapter(Context context, List<ParseUser> list) {
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(_context);
		View itemView = layoutInflater.inflate(R.layout.items_layout_for_slidemenu, null);

		final TextView tvUsername = (TextView) itemView.findViewById(R.id.tvshowName);
		final ImageView ivFace = (ImageView) itemView.findViewById(R.id.ivFace);

		final ParseUser user = _listName.get(position);

		ParseFile file = (ParseFile) user.getParseFile(UserData.PROFILE_PICTURE_KEY);

		file.getDataInBackground(new GetDataCallback() {

			@Override
			public void done(byte[] data, ParseException e) {
				if (e == null) {
					Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
					ivFace.setImageBitmap(image);

					tvUsername.setText(user.getString(UserData.NAME_KEY));
					tvUsername.setShadowLayer(2, 1, 1, R.color.gray);
				}
			}
		});
		return itemView;
	}

}
