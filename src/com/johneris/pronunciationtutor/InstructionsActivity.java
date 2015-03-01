package com.johneris.pronunciationtutor;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.johneris.pronunciationtutor.common.Constants;
import com.johneris.pronunciationtutor.common.MusicManager;
import com.johneris.pronunciationtutor.common.Recognizer;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class InstructionsActivity extends Activity implements
		RecognitionListener {

	/**
	 * boolean to continue playing music
	 */
	boolean continueMusic = true;

	/**
	 * button to Skip
	 */
	Button buttonSkip;

	/**
	 * button to Record
	 */
	Button buttonRecord;

	/**
	 * button to Play game
	 */
	Button buttonPlayNow;

	/**
	 * textView for number of tries left
	 */
	TextView textViewTriesLeftVal;

	/**
	 * image to display
	 */
	ImageView imageView;

	/**
	 * textView for option1
	 */
	TextView textViewOption1;

	/**
	 * textView for option2
	 */
	TextView textViewOption2;

	/**
	 * textView how to at the bottom
	 */
	TextView textViewHowTo;

	/**
	 * recognizer for Speech Recognition
	 */
	SpeechRecognizer recognizer;

	/**
	 * Hypothesis recognizerHypothesis of SpeechRecognizer recognizer
	 */
	Hypothesis recognizerHypothesis;

	/**
	 * boolean to know is recording
	 */
	boolean isListening;

	/**
	 * number of tries left
	 */
	int tries;

	/**
	 * current word
	 */
	String currWord;

	/**
	 * iteration number
	 */
	int iteration;

	@Override
	public void onCreate(Bundle state) {

		super.onCreate(state);

		/* Create a full screen window */

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.instructions_layout);

		/* Background Image */

		// adapt the image to the size of the display
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.menu_background), size.x, size.y,
				true);

		// fill the background ImageView with the resized image
		ImageView iv_background = (ImageView) findViewById(R.id.instructions_imageBackground);
		iv_background.setImageBitmap(bmp);

		buttonSkip = (Button) findViewById(R.id.game_buttonSkip);
		buttonSkip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				buttonRecord.setEnabled(false);
				textViewHowTo.setText("You skipped the word. "
						+ "I already made the number of "
						+ "tries back to 3 so you can try again.");
				// set score to 0 and skip
				int score = 0;
				endItem(score);
				buttonRecord.setEnabled(true);
			}
		});

		buttonRecord = (Button) findViewById(R.id.game_buttonRecord);
		buttonRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isListening) {
					// stop recording
					stopListening();
					buttonSkip.setEnabled(true);
				} else {
					// start recording
					buttonSkip.setEnabled(false);
					startListening();
				}
			}
		});
		buttonRecord.setEnabled(false);

		buttonPlayNow = (Button) findViewById(R.id.instructions_buttonPlayNow);
		buttonPlayNow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(InstructionsActivity.this,
						GameModeActivity.class);
				startActivity(intent);
				finish();
			}
		});

		textViewTriesLeftVal = (TextView) findViewById(R.id.game_textViewTriesLeftVal);

		imageView = (ImageView) findViewById(R.id.game_imageView);

		textViewOption1 = (TextView) findViewById(R.id.game_textViewOption1);
		textViewOption2 = (TextView) findViewById(R.id.game_textViewOption2);

		textViewHowTo = (TextView) findViewById(R.id.instructions_textViewHowTo);
		textViewHowTo.setText("You have 3 tries left! "
				+ "Your goal is to pronunce the right word based "
				+ "on the image. Remember, correct pronunciation! "
				+ "Press Record and start speaking. Try it! Good luck!");

		/* Initialize game components */

		isListening = false;

		// get recognizer from common.Recognizer.getSpeechRecognizer()
		recognizer = Recognizer.getSpeechRecognizer();

		iteration = 0;

		// start game
		game();
	}

	private void game() {

		/*
		 * if iteration is equal to common.Constants.itemsPerGame else continue
		 * game
		 */

		if (iteration == 1) {
			iteration = 0;
			game();
		} else {
			// get the random index
			int index = 0;

			// set textView of tries left to 3
			textViewTriesLeftVal.setText("3");

			// random boolean to randomize options
			Random rand = new Random();
			boolean randBool = rand.nextBoolean();

			// get and set the image to display
			try {
				InputStream ims = getAssets().open(
						Constants.easyImagesDir
								+ Constants.easyImage.get(index));
				Drawable d = Drawable.createFromStream(ims, null);
				imageView.setImageDrawable(d);
			} catch (IOException ex) {
			}

			// set option 1
			textViewOption1.setText(randBool ? Constants.easyCorrect.get(index)
					.replace("#grade5", "") : Constants.easyWrong.get(index));

			// set option 2
			textViewOption2.setText(randBool ? Constants.easyWrong.get(index)
					: Constants.easyCorrect.get(index).replace("#grade5", ""));

			// set up recognizer
			setUpRecognizer(Constants.easyCorrect.get(index).replace("#grade5", ""),
					Constants.easyThreshold.get(index));

			// set current word
			currWord = Constants.easyCorrect.get(index).replace("#grade5", "").toLowerCase();

			// set tries to 0
			tries = 0;
			// enable record button
			buttonRecord.setEnabled(true);
		}

	}

	/**
	 * set up recognizer to recognize word with Threshold threshold
	 */
	private void setUpRecognizer(String word, float threshold) {
		// set up recognizer
		recognizer = defaultSetup()
				.setAcousticModel(
						new File(Recognizer.getModelsDir(), "hmm/en-us-semi"))
				.setDictionary(
						new File(Recognizer.getModelsDir(), "dict/cmu07a.dic"))
				.setKeywordThreshold(threshold).getRecognizer();
		recognizer.addListener(this);

		// add keyphrase search for word
		recognizer.addKeyphraseSearch(Constants.KWS_SEARCH, word.toLowerCase());
	}

	/**
	 * start recording
	 */
	private void startListening() {
		textViewHowTo.setText("Start Speaking! I'm listening... "
				+ "Press Stop to stop recording.");

		isListening = true;
		recognizerHypothesis = null;

		// pause music
		MusicManager.pause();

		// start listening
		recognizer.startListening(Constants.KWS_SEARCH);

		buttonRecord.setText("Stop");
	}

	/**
	 * stop recording
	 */
	private void stopListening() {
		int score = 0;
		isListening = false;

		// stop recognizer
		recognizer.stop();

		// get recognized word
		String strRecognized = recognizerHypothesis != null ? recognizerHypothesis
				.getHypstr() : "";

		// if recognizer did not detect word
		// because of wrong pronunciation
		if (recognizerHypothesis == null) {
			Toast.makeText(getApplicationContext(),
					"Ooops! Wrong Pronunciation", Toast.LENGTH_SHORT).show();
			if (!(tries + 1 == 3)) {
				textViewHowTo.setText("Ooops... Try again. You can also "
						+ "press the Skip button but that would make "
						+ "your score 0 for that word.");
			}
		}

		// increment number of tries
		tries++;
		// set tries left textView
		textViewTriesLeftVal.setText("" + (3 - tries));

		// if recognized
		if (strRecognized.equals(currWord) || strRecognized.contains(currWord)) {
			textViewHowTo
					.setText("Congratulations! You pronounced the word right!"
							+ " I made the number of tries back to 3");
			// compute score
			score = 3 - (tries - 1);
			// end item with score
			endItem(score);
		} else if (tries == 3) {
			textViewHowTo.setText("You reached the number of tries per word. "
					+ "Sorry but you were not able to pronounce it right. "
					+ "You can try again. I already made the number of "
					+ "tries back to 3 for you.");
			// set score to 0
			score = 0;
			// end item with score
			endItem(score);
		}

		buttonRecord.setText("Record");

		// start playing music
		MusicManager.start(this, MusicManager.MUSIC_ALL);
	}

	/**
	 * end an item
	 */
	private void endItem(int score) {
		// show score
		showItemResultDialog(0, score);
		// continue game
		game();
	}

	/**
	 * show alert dialog with the word and score
	 */
	@SuppressLint("NewApi")
	private void showItemResultDialog(int index, int score) {
		String word = "";
		String pronunciation = "";

		word = Constants.easyCorrect.get(index).replace("#grade5", "");
		pronunciation = Constants.easyPhoneme.get(index);

		// build an alert dialog
		AlertDialog.Builder scoreDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.iteration_result_layout,
				null);
		scoreDialog.setView(dialogView);

		// word
		TextView textViewWordVal = (TextView) dialogView
				.findViewById(R.id.iterationresult_textViewWordVal);
		textViewWordVal.setText(word);

		// pronunciation
		TextView textViewPronunciationVal = (TextView) dialogView
				.findViewById(R.id.iterationresult_textViewPronunciationVal);
		textViewPronunciationVal.setText(pronunciation);

		// rating bar
		RatingBar ratingBar = (RatingBar) dialogView
				.findViewById(R.id.iterationresult_ratingBar);
		ratingBar.setRating(score);
		ratingBar.setEnabled(false);

		final String fWord = word;
		ImageButton buttonPronounce = (ImageButton) dialogView
				.findViewById(R.id.iterationresult_buttonPronounce);
		buttonPronounce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					MusicManager.pause();

					String strWavFile = "pronounce/";
					strWavFile += "easy/";
					strWavFile += fWord.toUpperCase() + ".wav";

					AssetFileDescriptor afd = getAssets().openFd(strWavFile);
					MediaPlayer player = new MediaPlayer();
					player.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(), afd.getLength());
					player.prepare();
					player.start();

					while (player.isPlaying())
						;

					MusicManager.start(InstructionsActivity.this,
							MusicManager.MUSIC_ALL);
				} catch (Exception e) {
				}
			}
		});

		// show alert dialog
		AlertDialog myAlert = scoreDialog.create();
		myAlert.show();
	}

	@Override
	public void onBackPressed() {
		// start MenuActivity and finish InstructionsActivity
		Intent intent = new Intent(InstructionsActivity.this,
				MenuActivity.class);
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

	/*
	 * get partial results from recognizer
	 */
	@Override
	public void onPartialResult(Hypothesis hypothesis) {
		// if hypothesis is not null
		// set recognizerHypothesis to hypothesis
		if (hypothesis != null) {
			recognizerHypothesis = hypothesis;
		}
	}

	/*
	 * get final result from recognizer
	 */
	@Override
	public void onResult(Hypothesis hypothesis) {
		// if hypothesis is not null
		// set recognizerHypothesis to hypothesis
		if (hypothesis != null) {
			recognizerHypothesis = hypothesis;
		}
	}

	@Override
	public void onBeginningOfSpeech() {
	}

	@Override
	public void onEndOfSpeech() {
	}

	@Override
	public void onError(Exception error) {
	}

	@Override
	public void onTimeout() {
	}

}
