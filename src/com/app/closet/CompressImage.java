package com.app.closet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CompressImage {

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	
	public static Bitmap decodeSampledBitmapFromFile(String path){
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		
		return bitmap;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(InputStream is,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
	//	 options.inJustDecodeBounds = true;
		 //Base64.encode(byteArray, Base64.DEFAULT);
		// BitmapFactory.decodeStream(is, null, options);

		// Calculate inSampleSize
		options.inSampleSize = 8; //calculateInSampleSize(options, reqWidth, reqHeight);
		
		// Decode bitmap with inSampleSize set
//		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
	
		return bitmap;
	}
	
	public static byte[] readFully(InputStream input) throws IOException
	{
	    byte[] buffer = new byte[8192];
	    int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    while ((bytesRead = input.read(buffer)) != -1)
	    {
	        output.write(buffer, 0, bytesRead);
	    }
	    return output.toByteArray();
	}
}
