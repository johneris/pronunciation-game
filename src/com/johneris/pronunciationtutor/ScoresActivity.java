package com.johneris.pronunciationtutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.johneris.pronunciationtutor.common.Constants;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScoresActivity extends Activity {

	boolean continueMusic = true;
	
	TableLayout tableLayout;
	
	Button buttonClear;
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
				buttonClear.setEnabled(false);
				buttonBackToMainMenu.setEnabled(false);
				Constants.lstUserProfile.clear();
				saveUserProfiles();
				loadTable();
				buttonClear.setEnabled(true);
				buttonBackToMainMenu.setEnabled(true);
			}
        });
        
        buttonBackToMainMenu = (Button) findViewById(R.id.scores_buttonBackToMainMenu);
        buttonBackToMainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScoresActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        loadTable();
	}
	
	
	
	private void loadTable() {
		
		loadUserProfiles();
		
		tableLayout.removeAllViews();
		
		int textViewWidth = 90;
		int textViewNameWidth = 100;
		int buttonDeleteWidth = 50;
		int buttonDeleteHeight = 50;
		
		LinearLayout linearLayout = new LinearLayout(getApplicationContext());
		
		TextView textViewNameLbl = new TextView(getApplicationContext());
    	textViewNameLbl.setText("Name");
    	textViewNameLbl.setWidth(textViewNameWidth);
    	
    	TextView textViewEasyLbl = new TextView(getApplicationContext());
    	textViewEasyLbl.setText("Easy");
    	textViewEasyLbl.setWidth(textViewWidth);
    	
    	TextView textViewNormalLbl = new TextView(getApplicationContext());
    	textViewNormalLbl.setText("Normal");
    	textViewNormalLbl.setWidth(textViewWidth);
    	
    	TextView textViewHardLbl = new TextView(getApplicationContext());
    	textViewHardLbl.setText("Hard");
    	textViewHardLbl.setWidth(textViewWidth);
    	
    	TextView dummy = new TextView(getApplicationContext());
    	dummy.setText("");
    	dummy.setWidth(buttonDeleteWidth);
    	
    	linearLayout.addView(textViewNameLbl);
    	linearLayout.addView(textViewEasyLbl);
    	linearLayout.addView(textViewNormalLbl);
    	linearLayout.addView(textViewHardLbl);
    	linearLayout.addView(dummy);
		
		TableRow tableRowLbl = new TableRow(getApplicationContext());
		tableRowLbl.addView(linearLayout);
		tableRowLbl.setGravity(Gravity.CENTER);
		
		tableLayout.addView(tableRowLbl);

		for(int i = 0; i < Constants.lstUserProfile.size(); i++) {
			final UserProfile userProfile = Constants.lstUserProfile.get(i);
			
			LinearLayout linearLayoutItem = new LinearLayout(getApplicationContext());
			
        	TextView textViewName = new TextView(getApplicationContext());
        	textViewName.setText(userProfile.getUserName());
        	textViewName.setWidth(textViewNameWidth);
        	
        	TextView textViewEasy = new TextView(getApplicationContext());
        	textViewEasy.setText("" + userProfile.getEasyScore());
        	textViewEasy.setWidth(textViewWidth);
        	
        	TextView textViewNormal = new TextView(getApplicationContext());
        	textViewNormal.setText("" + userProfile.getNormalScore());
        	textViewNormal.setWidth(textViewWidth);
        	
        	TextView textViewHard = new TextView(getApplicationContext());
        	textViewHard.setText("" + userProfile.getHardScore());
        	textViewHard.setWidth(textViewWidth);
        	
        	Button buttonDelete = new Button(getApplicationContext());
        	buttonDelete.setLayoutParams(new LinearLayout.LayoutParams(buttonDeleteWidth, buttonDeleteHeight));
        	buttonDelete.setPadding(2, 2, 2, 2);
        	buttonDelete.setText("x");
        	buttonDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Constants.lstUserProfile.remove(userProfile);
					saveUserProfiles();
					loadTable();
				}
        	});
        	
        	linearLayoutItem.addView(textViewName);
        	linearLayoutItem.addView(textViewEasy);
        	linearLayoutItem.addView(textViewNormal);
        	linearLayoutItem.addView(textViewHard);
        	linearLayoutItem.addView(buttonDelete);
			
			TableRow tableRow = new TableRow(getApplicationContext());
			tableRow.addView(linearLayoutItem);
			tableRow.setGravity(Gravity.CENTER);
			
			tableLayout.addView(tableRow);
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
			Toast.makeText(getApplicationContext(), "Saved", 
					Toast.LENGTH_SHORT).show();
		} catch(Throwable t) {
			Toast.makeText(getApplicationContext(), "Save failed", 
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	@Override
	public void onBackPressed() {
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
