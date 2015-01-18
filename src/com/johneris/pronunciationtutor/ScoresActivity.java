package com.johneris.pronunciationtutor;

import com.johneris.pronunciationtutor.common.Constants;
import com.johneris.pronunciationtutor.common.Keys;
import com.johneris.pronunciationtutor.common.MusicManager;
import com.johneris.pronunciationtutor.common.StoreUserProfiles;
import com.johneris.pronunciationtutor.common.UserProfile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoresActivity extends Activity {

	/**
	 * boolean to continue playing music
	 */
	boolean continueMusic = true;
	
	/**
	 * table for scores
	 */
	TableLayout tableLayout;
	
	/**
	 * button to clear all saved scores
	 */
	Button buttonClear;

	/**
	 * button to go back to Main Menu
	 */
	Button buttonBackToMainMenu;
	
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		/* Create a full screen window */
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.scores_layout);
		
		
		/* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
          getResources(),R.drawable.result_background),size.x,size.y,true);
        
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.scores_imageBackground);
        iv_background.setImageBitmap(bmp);
        
        
        /* Initialize views */
        
        tableLayout = (TableLayout) findViewById(R.id.scores_tableLayout);
        
        buttonClear = (Button) findViewById(R.id.scores_buttonClear);
        buttonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// disable buttons
				buttonClear.setEnabled(false);
				buttonBackToMainMenu.setEnabled(false);
				// clear all saved scores
				Constants.lstUserProfile.clear();
				StoreUserProfiles.saveToFile(getApplicationContext());
				// reload table
				loadTable();
				// enable buttons
				buttonClear.setEnabled(true);
				buttonBackToMainMenu.setEnabled(true);
			}
        });
        
        buttonBackToMainMenu = (Button) findViewById(R.id.scores_buttonBackToMainMenu);
        buttonBackToMainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// start MenuActivity and finish ScoresActivity
				Intent intent = new Intent(ScoresActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        loadTable();
	}
	
	
	
	/**
	 * load table of scores
	 */
	private void loadTable() {
		
		// remove all scores in table
		tableLayout.removeAllViews();
		
		int textViewNameWidth = 100;
		int buttonDeleteWidth = 50;
		int buttonDeleteHeight = 50;

		// fill table with user profiles
		for(int i = 0; i < Constants.lstUserProfile.size(); i++) {
			final UserProfile userProfile = Constants.lstUserProfile.get(i);
			
			// layout that will contain user profile
			LinearLayout linearLayoutItem = new LinearLayout(getApplicationContext());
			
        	TextView textViewName = new TextView(getApplicationContext());
        	textViewName.setText(userProfile.userName);
        	textViewName.setWidth(textViewNameWidth);
        	textViewName.setTextSize(15);
        	
        	Button buttonEasy = new Button(getApplicationContext());
        	buttonEasy.setText("Easy");
        	buttonEasy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(ScoresActivity.this, ScoresPreviewActivity.class);
					
					// send player name
					intent.putExtra(Keys.PLAYER_NAME, userProfile.userName);

					// send game mode
					intent.putExtra(Keys.GAME_MODE, Constants.GAMEMODE_EASY);
					
					startActivity(intent);
					finish();
				}
        	});
        	
        	Button buttonNormal = new Button(getApplicationContext());
        	buttonNormal.setText("Normal");
        	buttonNormal.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(ScoresActivity.this, ScoresPreviewActivity.class);
					
					// send player name
					intent.putExtra(Keys.PLAYER_NAME, userProfile.userName);

					// send game mode
					intent.putExtra(Keys.GAME_MODE, Constants.GAMEMODE_NORMAL);
					
					startActivity(intent);
					finish();
				}
        	});
        	
        	Button buttonHard = new Button(getApplicationContext());
        	buttonHard.setText("Hard");
        	buttonHard.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(ScoresActivity.this, ScoresPreviewActivity.class);
					
					// send player name
					intent.putExtra(Keys.PLAYER_NAME, userProfile.userName);

					// send game mode
					intent.putExtra(Keys.GAME_MODE, Constants.GAMEMODE_HARD);
					
					startActivity(intent);
					finish();
				}
        	});
        	
        	// delete profile button
        	Button buttonDelete = new Button(getApplicationContext());
        	buttonDelete.setLayoutParams(new LinearLayout.LayoutParams(buttonDeleteWidth, buttonDeleteHeight));
        	buttonDelete.setPadding(2, 2, 2, 2);
        	buttonDelete.setText("x");
        	buttonDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// delete profile
					Constants.lstUserProfile.remove(userProfile);
					// save
					StoreUserProfiles.saveToFile(getApplicationContext());
					// reload table
					loadTable();
				}
        	});
        	
        	// add to layout
        	linearLayoutItem.addView(textViewName);
        	linearLayoutItem.addView(buttonEasy);
        	linearLayoutItem.addView(buttonNormal);
        	linearLayoutItem.addView(buttonHard);
        	
        	if(userProfile.lstEasyResult.isEmpty()) {
        		buttonEasy.setEnabled(false);
        	}
        	if(userProfile.lstNormalResult.isEmpty()) {
        		buttonNormal.setEnabled(false);
        	}
        	if(userProfile.lstHardResult.isEmpty()) {
        		buttonHard.setEnabled(false);
        	}
        	linearLayoutItem.addView(buttonDelete);
			
			// add layout to table row
			TableRow tableRow = new TableRow(getApplicationContext());
			tableRow.addView(linearLayoutItem);
			tableRow.setGravity(Gravity.CENTER);
			
			// add table row to table
			tableLayout.addView(tableRow);
        }
	}
	
	
	
	@Override
	public void onBackPressed() {
		// start MenuActivity and finish ScoresActivity
		Intent intent = new Intent(ScoresActivity.this, MenuActivity.class);
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
