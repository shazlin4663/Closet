package com.app.closet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

	@Override
	protected Bitmap doInBackground(String... url) {
		try {
			return downloadURL(url[0]);
		} catch (IOException e) {
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
	}


	public Bitmap downloadURL(String myUrl) throws IOException {
		InputStream is = null;

		try {
			URL url = new URL(myUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(15000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
			
			is = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
	
		} finally {
			if (is != null) {
				is.close();
			} 
		}
	}

}