package com.johneris.pronunciationtutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.johneris.pronunciationtutor.common.Constants;
import com.johneris.pronunciationtutor.common.Keys;
import com.johneris.pronunciationtutor.common.MusicManager;
import com.johneris.pronunciationtutor.common.ResultWrapper;
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
import android.widget.Toast;

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
				saveUserProfiles();
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
        
        loadUserProfiles();
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
					saveUserProfiles();
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
	
	
	
	/**
	 * load user profiles from
	 * file common.Constants.userScoreFile
	 */
	private void loadUserProfiles() {
		if(!Constants.lstUserProfile.isEmpty())	return;
		
		// initialize Constants.lstUserProfile
		Constants.lstUserProfile = new ArrayList<>();
		
		try {
			// create InputStream in for Constants.userScoreFile file
			InputStream in = openFileInput(Constants.userScoreFile);
			
			if (in != null) {
				
				// initialize BufferedReader reader
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
				// store the line from file to str
				String str;
				
				// while not end of file, read one instance of userProfile
				while((str = reader.readLine()) != null) {
					
					// initialize instance of userProfile
					UserProfile userProfile = new UserProfile();
					
					// get the userName
					userProfile.userName = str;
					
					// for 3 game modes Easy, Normal, Hard 
					for(int mode = 0; mode < 3; mode++) {
						
						// read line for game mode and number of games played
						// gameMode numberOfGamesPlayed
						str = reader.readLine();
						// game mode
						String gameMode = str.split(" ")[0];
						// number of games played
						int numberOfGamesPlayed = Integer.parseInt(str.split(" ")[1]);
						
						// for numberOfGamesPlayed
						for(int n = 0; n < numberOfGamesPlayed; n++) {
							
							// read line of words and scores
							// word score word score ...
							str = reader.readLine();
							
							// split str
							String [] arrResult = str.split(" ");
							
							// initialize list of words
							ArrayList<String> lstWord = new ArrayList<>();
							// initialize list of scores
							ArrayList<Integer> lstScore = new ArrayList<>();
							
							// for all items per game
							for(int i = 0; i < Constants.itemsPerGame; i++) {
								// add word to list of words
								lstWord.add(arrResult[i]);
								// add score to list of scores
								lstScore.add(Integer.parseInt(arrResult[i+1]));
							}
							
							// create ResultWrapper resultWrapper
							ResultWrapper resultWrapper = new ResultWrapper();
							// add the list of words to resultWrapper
							resultWrapper.lstWord = new ArrayList<>(lstWord);
							// add the list of scores to resultWrapper
							resultWrapper.lstScore = new ArrayList<>(lstScore);
							
							// add resultWrapper according to game mode
							if(gameMode.equals(Constants.GAMEMODE_EASY)) {
								userProfile.lstEasyResult.add(resultWrapper);
							} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
								userProfile.lstNormalResult.add(resultWrapper);
							}  else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
								userProfile.lstHardResult.add(resultWrapper);
							} 
						}
					}
				}
				
				// close InputStream in
				in.close();
			}
		} catch (java.io.FileNotFoundException e) {
			// that's OK, we probably haven't created it 
		} catch (Throwable t) {
		}
	}
	
	
	
	/**
	 * save user profiles to file
	 */
	private void saveUserProfiles() {
		try {
			// create an OuputStreamWriter out to write to Constants.userScoreFile file
			OutputStreamWriter out = new OutputStreamWriter(
					openFileOutput(Constants.userScoreFile, 0));
			
			// save all data from common.Constants.lstUserProfile
			for(UserProfile userProfile : Constants.lstUserProfile) {
				
				// write user name
				// userProfile.userName
				out.write(userProfile.userName + "\n");
				
				// write scores in easy mode
				// Constants.GAMEMODE_EASY userProfile.lstEasyResult.size()
				// word score word score ...
				out.write(Constants.GAMEMODE_EASY + " "
						+ userProfile.lstEasyResult.size() + "\n");
				for(int i = 0; i < userProfile.lstEasyResult.size(); i++) {
					ResultWrapper resultWrapper = userProfile.lstEasyResult.get(i);
					for(int j = 0; j < resultWrapper.lstWord.size(); j++) {
						out.write(resultWrapper.lstWord.get(j) + " "
								+ resultWrapper.lstScore.get(j) + " ");
					}
					out.write("\n");
				}
				
				// write scores in normal mode
				// Constants.GAMEMODE_NORMAL userProfile.lstNormalResult.size()
				// word score word score ...
				out.write(Constants.GAMEMODE_NORMAL + " "
						+ userProfile.lstNormalResult.size() + "\n");
				for(int i = 0; i < userProfile.lstNormalResult.size(); i++) {
					ResultWrapper resultWrapper = userProfile.lstNormalResult.get(i);
					for(int j = 0; j < resultWrapper.lstWord.size(); j++) {
						out.write(resultWrapper.lstWord.get(j) + " "
								+ resultWrapper.lstScore.get(j) + " ");
					}
					out.write("\n");
				}
				
				// write scores in hard mode
				// Constants.GAMEMODE_HARD userProfile.lstHardResult.size()
				// word score word score ...
				out.write(Constants.GAMEMODE_HARD + " "
						+ userProfile.lstHardResult.size() + "\n");
				for(int i = 0; i < userProfile.lstHardResult.size(); i++) {
					ResultWrapper resultWrapper = userProfile.lstHardResult.get(i);
					for(int j = 0; j < resultWrapper.lstWord.size(); j++) {
						out.write(resultWrapper.lstWord.get(j) + " "
								+ resultWrapper.lstScore.get(j) + " ");
					}
					out.write("\n");
				}
			}
			
			// close OutputStreamWriter out
			out.close();
			
			// display "User data saved"
			Toast.makeText(getApplicationContext(), "User data saved", 
					Toast.LENGTH_SHORT).show();
		
		} catch(Throwable t) {
			// display "Save failed"
			Toast.makeText(getApplicationContext(), "Save failed", 
					Toast.LENGTH_SHORT).show();
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
