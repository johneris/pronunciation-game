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

	boolean continueMusic = true;
	
	TextView textViewNameVal;
	TextView textViewGameModeVal;
	
	TableLayout tableLayout;
	
	TextView textViewTotalScoreVal;
	
	Button buttonSave;
	Button buttonBackToMainMenu;
	
	String playerName;
	String gameMode;
	ArrayList<String> lstWord;
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
        	playerName = extras.containsKey(Keys.PLAYER_NAME) ? 
        			extras.getString(Keys.PLAYER_NAME) : "";
        	gameMode = extras.containsKey(Keys.GAME_MODE) ? 
        			extras.getString(Keys.GAME_MODE) : "";
        	lstWord = (ArrayList<String>) (extras.containsKey(Keys.GAME_WORDS) ?
        			extras.getStringArrayList(Keys.GAME_WORDS) : new ArrayList<>());
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
				saveUserData();
			}
		});
		
		buttonBackToMainMenu = (Button) findViewById(R.id.scores_buttonBackToMainMenu);
		buttonBackToMainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		tableLayout = (TableLayout) findViewById(R.id.scores_tableLayout);
		
		for(int i = 0; i < lstWord.size(); i++) {
			TextView textViewWord = new TextView(getApplicationContext());
			textViewWord.setText(lstWord.get(i));
			
			RatingBar ratingBarScore = new RatingBar(getApplicationContext());
			ratingBarScore.setEnabled(false);
			ratingBarScore.setNumStars(3);
			ratingBarScore.setRating(lstScore.get(i));
			
			TableRow tableRow = new TableRow(getApplicationContext());
			tableRow.addView(textViewWord);
			tableRow.addView(ratingBarScore);
			tableRow.setGravity(Gravity.CENTER);
			
			tableLayout.addView(tableRow);
		}
	}
	
	
	
	private void saveUserData() {
		
		boolean isSaved = false;
		
		loadUserProfiles();
		
		// find user if exist
		for(UserProfile userProfile : Constants.lstUserProfile) {
			if(userProfile.getUserName().equals(playerName)) {
				if(gameMode.equals(Constants.GAMEMODE_EASY)) {
					userProfile.setEasyScore(totalScore());
				} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
					userProfile.setNormalScore(totalScore());
				} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
					userProfile.setHardScore(totalScore());
				}
				isSaved = true;
			}
		}
		
		// well this is a new user
		if(!isSaved) {
			UserProfile user = new UserProfile();
			user.setUserName(playerName);
			user.setEasyScore(gameMode.equals(Constants.GAMEMODE_EASY) ? 
					totalScore() : 0);
			user.setNormalScore(gameMode.equals(Constants.GAMEMODE_NORMAL) ? 
					totalScore() : 0);
			user.setHardScore(gameMode.equals(Constants.GAMEMODE_HARD) ? 
					totalScore() : 0);
			Constants.lstUserProfile.add(user);
		}
		
		saveUserProfiles();
	}
	
	
	
	private void saveUserProfiles() {
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					openFileOutput(Constants.userScoreFile, 0));
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
					Constants.lstUserProfile.add(userProfile);
				}
				in.close();
			}
		} catch (java.io.FileNotFoundException e) {
			// that's OK, we probably haven't created it 
		} catch (Throwable t) {
		}
	}
	
	
	
	public int totalScore() {
		int total = 0;
		for(int s : lstScore) {
			total += s;
		}
		return total;
	}
	
	
	
	@Override
	public void onBackPressed() {
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
