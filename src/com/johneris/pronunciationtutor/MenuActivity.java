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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MenuActivity extends Activity {

	boolean continueMusic = true;
	
	Button buttonPlay;
	Button buttonScores;
	Button buttonInstructions;
	
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle state) {
		
		super.onCreate(state);
		
		
		/* Create a full screen window */
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.menu_layout);
		
		
		/* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
          getResources(),R.drawable.menu_background),size.x,size.y,true);
        
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.menu_imageBackground);
        iv_background.setImageBitmap(bmp);
		
        
        /* Initialize views */
        
		buttonPlay = (Button) findViewById(R.id.menu_buttonPlay);
		buttonPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				Intent intent = new Intent(MenuActivity.this, GameModeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		buttonScores = (Button) findViewById(R.id.menu_buttonScores);
		buttonScores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				Intent intent = new Intent(MenuActivity.this, ScoresActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		buttonInstructions = (Button) findViewById(R.id.menu_buttonInstructions);
		buttonInstructions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				Intent intent = new Intent(MenuActivity.this, InstructionsActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	
	
	
	private void disableButtons() {
		buttonPlay.setEnabled(false);
		buttonScores.setEnabled(false);
		buttonInstructions.setEnabled(false);
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
