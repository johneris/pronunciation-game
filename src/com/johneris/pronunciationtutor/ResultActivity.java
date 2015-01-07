package com.johneris.pronunciationtutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.johneris.pronunciationtutor.common.Constants;
import com.johneris.pronunciationtutor.common.Keys;
import com.johneris.pronunciationtutor.common.MusicManager;
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

	/* boolean to continue playing music
	*/
	boolean continueMusic = true;

	/* textView for playerName
	*/
	TextView textViewNameVal;

	/* textView for gameMode
	*/
	TextView textViewGameModeVal;
	
	/* table to display result
	*/
	TableLayout tableLayout;
	
	/* textView for total score
	*/
	TextView textViewTotalScoreVal;
	
	/* button to Save score
	*/
	Button buttonSave;

	/* button to go back to Main Menu
	*/
	Button buttonBackToMainMenu;
	
	/* player name
	*/
	String playerName;
	
	/* game mode
	*/
	String gameMode;
	
	/* list of words
	*/
	ArrayList<String> lstWord;

	/* list of scores
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
	
	
	
	/* save user data from 
	 * the results of the game
	*/
	private void saveUserData() {
		
		// boolean to indicate if user exists
		boolean isSaved = false;
		
		// load user profiles
		loadUserProfiles();
		
		// find user if exists
		for(UserProfile userProfile : Constants.lstUserProfile) {
			// if userName exists
			if(userProfile.getUserName().equals(playerName)) {
				// set score according to game mode
				if(gameMode.equals(Constants.GAMEMODE_EASY)) {
					userProfile.setEasyScore(totalScore());
				} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
					userProfile.setNormalScore(totalScore());
				} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
					userProfile.setHardScore(totalScore());
				}
				// set isSaved to true
				isSaved = true;
			}
		}
		
		// well this is a new user
		if(!isSaved) {
			// create a new user
			UserProfile user = new UserProfile();
			// set user name
			user.setUserName(playerName);
			// set easy score
			user.setEasyScore(gameMode.equals(Constants.GAMEMODE_EASY) ? 
					totalScore() : 0);
			// set normal score
			user.setNormalScore(gameMode.equals(Constants.GAMEMODE_NORMAL) ? 
					totalScore() : 0);
			// set hard score
			user.setHardScore(gameMode.equals(Constants.GAMEMODE_HARD) ? 
					totalScore() : 0);
			// add to the list of user profiles
			Constants.lstUserProfile.add(user);
		}
		
		// save user profiles
		saveUserProfiles();
	}
	
	
	
	/* save user profiles to file
	*/
	private void saveUserProfiles() {
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					openFileOutput(Constants.userScoreFile, 0));
			// save all data from common.Constants.lstUserProfile
			for(UserProfile userProfile : Constants.lstUserProfile) {
				out.write(userProfile.getUserName() + " " +
							userProfile.getEasyScore() + " " +
							userProfile.getNormalScore() + " " +
							userProfile.getHardScore() + " " +
							"\n");
			}
			out.close();
			Toast.makeText(getApplicationContext(), "User data saved", 
					Toast.LENGTH_SHORT).show();
		} catch(Throwable t) {
			Toast.makeText(getApplicationContext(), "Save failed", 
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	/* load user profiles from
	 * file common.Constants.userScoreFile
	*/
	private void loadUserProfiles() {
		if(!Constants.lstUserProfile.isEmpty()) return;
		Constants.lstUserProfile = new ArrayList<>();
		try {
			InputStream in = openFileInput(Constants.userScoreFile);
			if (in != null) {
				InputStreamReader tmp = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(tmp);
				String str;
				while ((str = reader.readLine()) != null) {
					String [] user = str.split(" ");
					UserProfile userProfile = new UserProfile(user[0],
							Integer.parseInt(user[1]),	// easy score
							Integer.parseInt(user[2]),	// normal score
							Integer.parseInt(user[3]));	// hard score
					// add to common.Constants.lstUserProfile
					Constants.lstUserProfile.add(userProfile);
				}
				in.close();
			}
		} catch (java.io.FileNotFoundException e) {
			// that's OK, we probably haven't created it 
		} catch (Throwable t) {
		}
	}
	
	
	
	/* return the total score
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
