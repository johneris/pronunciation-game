package com.johneris.pronunciationtutor;

import com.johneris.pronunciationtutor.common.MusicManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class InstructionsActivity extends Activity {

	boolean continueMusic = true;
	
	
	
	@Override
	public void onCreate(Bundle state) {
		
		super.onCreate(state);
		
		/* Create a full screen window */
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.instructions_layout);
		
		
		/* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
          getResources(),R.drawable.menu_background),size.x,size.y,true);
        
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.instructions_imageBackground);
        iv_background.setImageBitmap(bmp);
		
	}
	


	@Override
	public void onBackPressed() {
		Intent intent = new Intent(InstructionsActivity.this, MenuActivity.class);
    	startActivity(intent);
    	finish();
    	super.onBackPressed();
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		if (!continueMusic) {
			MusicManager.pause();
		}
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		continueMusic = false;
		MusicManager.start(this, MusicManager.MUSIC_ALL);
	}
	
}
