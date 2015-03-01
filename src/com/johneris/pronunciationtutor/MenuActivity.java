package com.johneris.pronunciationtutor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.johneris.pronunciationtutor.common.MusicManager;

public class MenuActivity extends Activity {

	/**
	 * boolean to continue playing music
	 */
	boolean continueMusic = true;

	/**
	 * textView Watch Tutorial
	 */
	TextView textViewWatchTutorial;

	/**
	 * button to Play
	 */
	Button buttonPlay;

	/**
	 * button to view Scores
	 */
	Button buttonScores;

	/**
	 * button to view instructions
	 */
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
				getResources(), R.drawable.menu_background), size.x, size.y,
				true);

		// fill the background ImageView with the resized image
		ImageView iv_background = (ImageView) findViewById(R.id.menu_imageBackground);
		iv_background.setImageBitmap(bmp);

		/* Initialize views */

		textViewWatchTutorial = (TextView) findViewById(R.id.menu_textViewWatchTutorial);
		textViewWatchTutorial.setClickable(true);
		SpannableString content = new SpannableString("Watch Tutorial");
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		textViewWatchTutorial.setText(content);
		textViewWatchTutorial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// start VideoActivity and finish MenuActivity
				Intent intent = new Intent(MenuActivity.this,
						VideoActivity.class);
				startActivity(intent);
				finish();
			}
		});

		buttonPlay = (Button) findViewById(R.id.menu_buttonPlay);
		buttonPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				// start GameModeActivity and finish MenuActivity
				Intent intent = new Intent(MenuActivity.this,
						GameModeActivity.class);
				startActivity(intent);
				finish();
			}
		});

		buttonScores = (Button) findViewById(R.id.menu_buttonScores);
		buttonScores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				// start ScoresActivity and finish MenuActivity
				Intent intent = new Intent(MenuActivity.this,
						ScoresActivity.class);
				startActivity(intent);
				finish();
			}
		});

		buttonInstructions = (Button) findViewById(R.id.menu_buttonInstructions);
		buttonInstructions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				disableButtons();
				// start InstructionsActivity and finish MenuActivity
				Intent intent = new Intent(MenuActivity.this,
						InstructionsActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	/**
	 * disable all buttons
	 */
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

	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MenuActivity.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}

}
