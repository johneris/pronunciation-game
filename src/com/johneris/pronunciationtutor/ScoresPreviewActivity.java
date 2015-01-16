package com.johneris.pronunciationtutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoresPreviewActivity extends Activity {

	/**
	 * boolean to continue playing music
	 */
	boolean continueMusic = true;

	/**
	 * textView for playerName
	 */
	TextView textViewNameVal;

	/**
	 * textView for gameMode
	 */
	TextView textViewGameModeVal;
	
	/**
	 * textView number of times played
	 */
	TextView textViewTimesPlayed;
	
	/**
	 * table to display result
	 */
	TableLayout tableLayout;
	
	/**
	 * textView for total score
	 */
	TextView textViewTotalScoreVal;
	
	/**
	 * button to view previous game results
	 */
	Button buttonPrevious;
	
	/**
	 * button to view next game results
	 */
	Button buttonNext;
	
	/**
	 * user profile
	 */
	UserProfile userProfile;
	
	/**
	 * list of result wrapper
	 */
	ArrayList<ResultWrapper> lstResultWrapper;
	
	/**
	 * current index of game played
	 */
	int currIndex;
		
	/**
	 * player name
	 */
	String playerName;
	
	/**
	 * game mode
	 */
	String gameMode;
	
	
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle state) {
		
		super.onCreate(state);
		
		
		/* Create a full screen window */
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.scores_preview_layout);
		
		
		/* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
          getResources(),R.drawable.result_background),size.x,size.y,true);
        
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.scorespreview_imageBackground);
        iv_background.setImageBitmap(bmp);
		
        
        /* Get extras */
        
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
        } else {
        	// get the player name
        	playerName = extras.containsKey(Keys.PLAYER_NAME) ? 
        			extras.getString(Keys.PLAYER_NAME) : "";
        	
        	// get the game mode
        	gameMode = extras.containsKey(Keys.GAME_MODE) ? 
        			extras.getString(Keys.GAME_MODE) : "";
        }
		
        
        /* Initialize Views */
        
		textViewNameVal = (TextView) findViewById(R.id.result_textViewNameVal);
		textViewNameVal.setText(playerName);
		
		textViewGameModeVal = (TextView) findViewById(R.id.result_textViewGameModeVal);
		textViewGameModeVal.setText(gameMode);
		
		textViewTimesPlayed = (TextView) findViewById(R.id.scorespreview_textViewTimesPlayed);
		
		textViewTotalScoreVal = (TextView) findViewById(R.id.result_textViewTotalScoreVal);
		
		buttonPrevious = (Button) findViewById(R.id.scorespreview_buttonPrevious);
		buttonPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				currIndex--;
				if(currIndex < 0) {
					currIndex = 0;
				} else {
					loadTable();
				}
				manageEnableOfButtons();
			}
		});
		
		buttonNext = (Button) findViewById(R.id.scorespreview_buttonNext);
		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				currIndex++;
				if(currIndex >= lstResultWrapper.size()) {
					currIndex = lstResultWrapper.size()-1;
				} else {
					loadTable();
				}
				manageEnableOfButtons();
			}
		});
		
		tableLayout = (TableLayout) findViewById(R.id.scores_tableLayout);
		
		for(UserProfile u : Constants.lstUserProfile) {
			if(u.userName.equals(playerName)) {
				this.userProfile = u;
			}
		}
		
		if(gameMode.equals(Constants.GAMEMODE_EASY)) {
			lstResultWrapper = userProfile.lstEasyResult;
			currIndex = userProfile.lstEasyResult.size()-1;
		} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
			lstResultWrapper = userProfile.lstNormalResult;
			currIndex = userProfile.lstNormalResult.size()-1;
		} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
			lstResultWrapper = userProfile.lstHardResult;
			currIndex = userProfile.lstHardResult.size()-1;
		}
		
		manageEnableOfButtons();
		
		textViewTimesPlayed.setText(" (" + lstResultWrapper.size()
				+ " times played)" );
		
		loadUserProfiles();
		loadTable();
	}
	
	
	
	/**
	 * manageButtons
	 */
	private void manageEnableOfButtons() {
		// in last index
		if(currIndex == lstResultWrapper.size()-1) {
			buttonNext.setEnabled(false);
		} else {
			buttonNext.setEnabled(true);
		}
		// in 0 index
		if(currIndex == 0) {
			buttonPrevious.setEnabled(false);
		} else {
			buttonPrevious.setEnabled(true);
		}
	}
	
	/**
	 * load table containing results
	 */
	private void loadTable() {
		tableLayout.removeAllViews();
		
		// fill table with results
		for(int i = 0; i < lstResultWrapper.get(currIndex).lstWord.size(); i++) {
					
			// word
			TextView textViewWord = new TextView(getApplicationContext());
			textViewWord.setText(lstResultWrapper.get(currIndex).lstWord.get(i));
			
			// score
			RatingBar ratingBarScore = new RatingBar(getApplicationContext());
			ratingBarScore.setEnabled(false);
			ratingBarScore.setNumStars(3);
			ratingBarScore.setRating(lstResultWrapper.get(currIndex).lstScore.get(i));
			
			// create a table row
			TableRow tableRow = new TableRow(getApplicationContext());
			
			// if perfect score add badge
			if(lstResultWrapper.get(currIndex).lstScore.get(i) == 3) {
				ImageView imageBadge = new ImageView(getApplicationContext());
				imageBadge.setImageResource(R.drawable.badge);
				tableRow.addView(imageBadge);
			// else add some dummy text to fill the column
			} else {
				TextView textViewDummy = new TextView(getApplicationContext());
				textViewDummy.setText("");
				tableRow.addView(textViewDummy);
			}
			
			tableRow.addView(textViewWord);
			tableRow.addView(ratingBarScore);
			tableRow.setGravity(Gravity.CENTER);
			
			// add the row to table
			tableLayout.addView(tableRow);
		}
		textViewTotalScoreVal.setText("" + totalScore());
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
	 * return the total score
	 */
	public int totalScore() {
		int total = 0;
		for(int s : lstResultWrapper.get(currIndex).lstScore) {
			total += s;
		}
		return total;
	}
	
	
	
	public void onBackPressed() {
		// start MenuActivity and finish ScoresActivity
		Intent intent = new Intent(ScoresPreviewActivity.this, ScoresActivity.class);
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
