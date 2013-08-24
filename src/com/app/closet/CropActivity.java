package com.app.closet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import eu.janmuller.android.simplecropimage.CropImage;

public class CropActivity extends Activity {

    public static final String TAG = "MainActivity";

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    
    public static final int REQUEST_CODE_GALLERY      = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;

    private ImageView mImageView;
    private File      mFileTemp;
    private Bitmap mFinalBitmap;
    private ParseUser parseUser;

    private LinearLayout buttonGroupTop;
    private LinearLayout buttonGroup1 ;
    private LinearLayout buttonGroup2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.activity_crop);
        parseUser = ParseUser.getCurrentUser();
        buttonGroupTop = (LinearLayout) findViewById(R.id.buttonGroupTop);
        buttonGroup1 = (LinearLayout) findViewById(R.id.buttonGroup1);
        buttonGroup2 = (LinearLayout) findViewById(R.id.buttonGroup2);

        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });

        findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePicture();
            }
        });
        
        
        Button topButton = (Button) findViewById(R.id.topButton);
        
        topButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResultAndFinish(Types.Top);
			}
		});
        
        Button buttomButton = (Button) findViewById(R.id.bottomButton);
        
        buttomButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResultAndFinish(Types.Bottom);
			}
		});

        Button shoeButton = (Button) findViewById(R.id.shoeButton);
        
        shoeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResultAndFinish(Types.Shoe);
			}
		});
        
        Button accessoryButton = (Button) findViewById(R.id.accessoryButton);
        
        accessoryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResultAndFinish(Types.Accessory);
			}
		});
        
        mImageView = (ImageView) findViewById(R.id.image);
        
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    		mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
    	}
    	else {
    		mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
    	}

    }
    
    private void setResultAndFinish(Types types)
    {
    	Intent res = new Intent();
    	res.putExtra("image", mFinalBitmap);
    	res.putExtra("type", types.name());
    	setResult(RESULT_OK, res); 
    	finish();
    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
        	Uri mImageCaptureUri = null;
        	String state = Environment.getExternalStorageState();
        	if (Environment.MEDIA_MOUNTED.equals(state)) {
        		mImageCaptureUri = Uri.fromFile(mFileTemp);
        	}
        	else {
	        	/*
	        	 * The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 */
	        	mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
        	}	
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {

            Log.d(TAG, "cannot take picture", e);
        }
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void startCropImage() {

        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {

            return;
        }

        Bitmap bitmap;

        switch (requestCode) {

            case REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {

                    Log.e(TAG, "Error while creating temp file", e);
                }

                break;
            case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;
            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }

        
              
                bitmap = CompressImage.decodeSampledBitmapFromFile(mFileTemp.getPath());
                
                mImageView.setImageBitmap(bitmap);
                mFinalBitmap = bitmap;
                
                buttonGroupTop.setVisibility(View.GONE);
                buttonGroup1.setVisibility(View.VISIBLE);
                buttonGroup2.setVisibility(View.VISIBLE);
                
                break;
                
        }
        super.onActivityResult(requestCode, resultCode, data);
        
    }
    
    
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

}
