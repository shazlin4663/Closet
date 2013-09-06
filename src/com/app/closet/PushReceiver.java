package com.app.closet;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

public class PushReceiver extends BroadcastReceiver {
	private static final String	TAG		= "MyCustomReceiver";
	private String data ="";
	public static int dataCount = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
	//		String action = intent.getAction();
		//	String channel = intent.getExtras().getString("com.parse.Channel");
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			
			Iterator itr = json.keys();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				if (key.equals("data")) {
					JSONArray array = json.getJSONArray(key);
					for (int x = 0; x < array.length(); x++) {
						data = data.concat(array.getString(x) + ",");
					}
					Log.d(TAG, "..." + key + " => " + json.getString(key));
				}
			}
			
			SharedPreferences pref = context.getSharedPreferences(NotificationActivity.PREF, 0);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("data" + dataCount, data);
			dataCount++;
			editor.commit();
			
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
	
}
