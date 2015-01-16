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
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

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
	 * table to display result
	 */
	TableLayout tableLayout;
	
	/**
	 * textView for total score
	 */
	TextView textViewTotalScoreVal;
	
	/**
	 * button to save score
	 */
	Button buttonSave;

	/** 
	 * button to go back to main menu
	 */
	Button buttonBackToMainMenu;
	
	/**
	 * player name
	 */
	String playerName;
	
	/**
	 * game mode
	 */
	String gameMode;
	
	/**
	 * list of words
	 */
	ArrayList<String> lstWord;

	/**
	 * list of scores
	 */
	ArrayList<Integer> lstScore;
	
	
	
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle state) {
		
		super.onCreate(state);
		
		
		/* Create a full screen window */
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.result_layout);
		
		
		/* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
          getResources(),R.drawable.result_background),size.x,size.y,true);
        
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.result_imageBackground);
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
        	
        	// get the list of words
        	lstWord = (ArrayList<String>) (extras.containsKey(Keys.GAME_WORDS) ?
        			extras.getStringArrayList(Keys.GAME_WORDS) : new ArrayList<>());

        	// get the list of scores
        	lstScore = (ArrayList<Integer>) (extras.containsKey(Keys.GAME_SCORES) ?
        			extras.getStringArrayList(Keys.GAME_SCORES) : new ArrayList<>());
        }
		
        
        /* Initialize Views */
        
		textViewNameVal = (TextView) findViewById(R.id.result_textViewNameVal);
		textViewNameVal.setText(playerName);
		
		textViewGameModeVal = (TextView) findViewById(R.id.result_textViewGameModeVal);
		textViewGameModeVal.setText(gameMode);
		
		textViewTotalScoreVal = (TextView) findViewById(R.id.result_textViewTotalScoreVal);
		textViewTotalScoreVal.setText("" + totalScore());
		
		buttonSave = (Button) findViewById(R.id.scores_buttonClear);
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// save user data
				saveUserData();
			}
		});
		
		buttonBackToMainMenu = (Button) findViewById(R.id.scores_buttonBackToMainMenu);
		buttonBackToMainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// start MenuActivity and finish ResultActivity
				Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		tableLayout = (TableLayout) findViewById(R.id.scores_tableLayout);
		
		// fill table with results
		for(int i = 0; i < lstWord.size(); i++) {
			// word
			TextView textViewWord = new TextView(getApplicationContext());
			textViewWord.setText(lstWord.get(i));
			
			// score
			RatingBar ratingBarScore = new RatingBar(getApplicationContext());
			ratingBarScore.setEnabled(false);
			ratingBarScore.setNumStars(3);
			ratingBarScore.setRating(lstScore.get(i));
			
			// create a table row that contains the word and score
			TableRow tableRow = new TableRow(getApplicationContext());
			tableRow.addView(textViewWord);
			tableRow.addView(ratingBarScore);
			tableRow.setGravity(Gravity.CENTER);
			
			// add the row to table
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
	 * save user data from 
	 * the results of the game
	 */
	private void saveUserData() {
		
		loadUserProfiles();
		
		// boolean to indicate if user exists
		boolean isSaved = false;
		
		// find user if exists
		for(UserProfile userProfile : Constants.lstUserProfile) {
			
			// if userName exists
			if(userProfile.userName.equals(playerName)) {
				
				// create a ResultWrapper resultWrapper to wrap the result
				ResultWrapper resultWrapper = new ResultWrapper();
				resultWrapper.lstWord = new ArrayList<>(this.lstWord);
				resultWrapper.lstScore = new ArrayList<>(this.lstScore);
				
				// add score according to game mode
				if(gameMode.equals(Constants.GAMEMODE_EASY)) {
					userProfile.lstEasyResult.add(resultWrapper);
				} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
					userProfile.lstNormalResult.add(resultWrapper);
				} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
					userProfile.lstHardResult.add(resultWrapper);
				}
				
				// set isSaved to true
				isSaved = true;
			}
		}
		
		
		// if not saved, this is a new user
		if(!isSaved) {
			
			// create a new user
			UserProfile user = new UserProfile();
			
			// set user name
			user.userName = playerName;
			
			// create a ResultWrapper resultWrapper to wrap the result
			ResultWrapper resultWrapper = new ResultWrapper();
			resultWrapper.lstWord = new ArrayList<>(this.lstWord);
			resultWrapper.lstScore = new ArrayList<>(this.lstScore);
			
			// add score according to game mode
			if(gameMode.equals(Constants.GAMEMODE_EASY)) {
				user.lstEasyResult.add(resultWrapper);
			} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
				user.lstNormalResult.add(resultWrapper);
			} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
				user.lstHardResult.add(resultWrapper);
			}
			
			// add to the list of user profiles
			Constants.lstUserProfile.add(user);
		}
		
		// save user profiles
		saveUserProfiles();
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
	
	
	
	/**
	 * return the total score
	 */
	public int totalScore() {
		int total = 0;
		for(int s : lstScore) {
			total += s;
		}
		return total;
	}
	
	
	
	@Override
	public void onBackPressed() {
		// start MenuActivity and finish ResultActivity
		Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
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
