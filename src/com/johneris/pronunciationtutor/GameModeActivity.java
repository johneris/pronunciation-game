package com.johneris.pronunciationtutor;

import com.johneris.pronunciationtutor.common.Constants;
import com.johneris.pronunciationtutor.common.Keys;
import com.johneris.pronunciationtutor.common.MusicManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class GameModeActivity extends Activity {

	/* boolean to continue playing music 
	*/
	boolean continueMusic = true;
	
	/* editText for input user name 
	*/
	EditText editTextName;

	/* button for easy mode
	*/
	Button buttonEasy;

	/* button for normal mode 
	*/
	Button buttonNormal;

	/* button for hard mode 
	*/
	Button buttonHard;
	
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle state) {
		
		super.onCreate(state);
		
		
		/* Create a full screen window */
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.gamemode_layout);
		
		
		/* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
          getResources(),R.drawable.menu_background),size.x,size.y,true);
        
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.gamemode_imageBackground);
        iv_background.setImageBitmap(bmp);
		
        
        /* Initialize views */
        
        editTextName = (EditText) findViewById(R.id.gamemode_editTextName);
		editTextName.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence name, int arg1, int arg2,
					int arg3) {
				if(name.toString().equals("")) {
					// disable buttons if player name is not entered
					disableButtons();
				} else {
					// enable buttons
					buttonEasy.setEnabled(true);
					buttonNormal.setEnabled(true);
					buttonHard.setEnabled(true);
				}
			}
		});
		
		buttonEasy = (Button) findViewById(R.id.gamemode_buttonEasy);
		buttonEasy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				// start GameActivity and finish GameModeActivity
				Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
				// send player name and game mode to GameActivity
				intent.putExtra(Keys.PLAYER_NAME, editTextName.getText().toString());
				intent.putExtra(Keys.GAME_MODE, Constants.GAMEMODE_EASY);
				startActivity(intent);
				GameModeActivity.this.finish();
			}
		});
		
		buttonNormal = (Button) findViewById(R.id.gamemode_buttonNormal);
		buttonNormal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				// start GameActivity and finish GameModeActivity
				Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
				// send player name and game mode to GameActivity
				intent.putExtra(Keys.PLAYER_NAME, editTextName.getText().toString());
				intent.putExtra(Keys.GAME_MODE, Constants.GAMEMODE_NORMAL);
				startActivity(intent);
				GameModeActivity.this.finish();
			}
		});
		
		buttonHard = (Button) findViewById(R.id.gamemode_buttonHard);
		buttonHard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				// start GameActivity and finish GameModeActivity
				Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
				// send player name and game mode to GameActivity
				intent.putExtra(Keys.PLAYER_NAME, editTextName.getText().toString());
				intent.putExtra(Keys.GAME_MODE, Constants.GAMEMODE_HARD);
				startActivity(intent);
				GameModeActivity.this.finish();
			}
		});
		
		// disable buttons when started
		// enable only after player name input
		disableButtons();
	}
	
	
	
	/* disable all buttons 
	*/
	private void disableButtons() {
		buttonEasy.setEnabled(false);
		buttonNormal.setEnabled(false);
		buttonHard.setEnabled(false);
	}
	
	
	
	@Override
	public void onBackPressed() {
		// start MenuActivity and finish GameModeActivity
		Intent intent = new Intent(GameModeActivity.this, MenuActivity.class);
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
