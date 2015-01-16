package com.johneris.pronunciationtutor;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import com.johneris.pronunciationtutor.common.Constants;
import com.johneris.pronunciationtutor.common.Keys;
import com.johneris.pronunciationtutor.common.MusicManager;
import com.johneris.pronunciationtutor.common.Randomizer;
import com.johneris.pronunciationtutor.common.Recognizer;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

public class GameActivity extends Activity implements
        RecognitionListener {
	
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
	 * button to Skip 
	 */
	Button buttonSkip;

	/**
	 * button to Record 
	 */
	Button buttonRecord;
	
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
	 * recognizer for Speech Recognition 
	 */
	SpeechRecognizer recognizer;

	/**
	 * Hypothesis recognizerHypothesis 
	 * of SpeechRecognizer recognizer  
	 */
	Hypothesis recognizerHypothesis;

	/**
	 * boolean to know is recording 
	 */
	boolean isListening;
	
	/**
	 * game mode
	 */
	String gameMode;

	/**
	 * player name
	 */
	String playerName;
	
	/**
	 * iteration number
	 */
	int iteration;

	/**
	 * number of tries left
	 */
	int tries;

	/**
	 * current word
	 */
	String currWord;
	
	/**
	 * list of random indexes
	 */
	ArrayList<Integer> lstRandIndex;

	/**
	 * list of scores
	 */
	ArrayList<Integer> lstScore;

	/**
	 * list of words
	 */
	ArrayList<String> lstWord;
	
	
	
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle state) {
        
		super.onCreate(state);
        
        /* Create a full screen window */
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.game_layout);
        
        
        /* Get extras */
        
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
        } else {
        	// get player name
        	playerName = extras.containsKey(Keys.PLAYER_NAME) ? 
        		extras.getString(Keys.PLAYER_NAME) : "";
        	// get game mode
        	gameMode = extras.containsKey(Keys.GAME_MODE) ? 
        		extras.getString(Keys.GAME_MODE) : "";
        }
        
        
        /* Background Image */
        
        // adapt the image to the size of the display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp;
        
        if(gameMode.equals(Constants.GAMEMODE_EASY)) {
        	bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    getResources(),R.drawable.game_easy_background),size.x,size.y,true);
        } else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
        	bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    getResources(),R.drawable.game_normal_background),size.x,size.y,true);
        } else {
        	bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    getResources(),R.drawable.game_hard_background),size.x,size.y,true);
        }
              
        // fill the background ImageView with the resized image
        ImageView iv_background = (ImageView) findViewById(R.id.game_imageBackground);
        iv_background.setImageBitmap(bmp);
        
        
        /* Initialize views */
        
        textViewNameVal = (TextView) findViewById(R.id.game_textViewNameVal);
        textViewNameVal.setText(playerName);
        
        textViewGameModeVal = (TextView) findViewById(R.id.game_textViewGameModeVal);
        textViewGameModeVal.setText(gameMode);
        
        buttonSkip = (Button) findViewById(R.id.game_buttonSkip);
        buttonSkip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				buttonRecord.setEnabled(false);
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
				if(isListening) {
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
        
        textViewTriesLeftVal = (TextView) findViewById(R.id.game_textViewTriesLeftVal);
        
        imageView = (ImageView) findViewById(R.id.game_imageView);
        
        textViewOption1 = (TextView) findViewById(R.id.game_textViewOption1);
        textViewOption2 = (TextView) findViewById(R.id.game_textViewOption2);
        
        
        /* Initialize game components */
        
        isListening = false;

        // get recognizer from common.Recognizer.getSpeechRecognizer()
        recognizer = Recognizer.getSpeechRecognizer();

        iteration = 0;
        
        // number of stored words
        int wordsNum = 0;
        
        // get the number of stored words according to game mode
        if(gameMode.equals(Constants.GAMEMODE_EASY)) {
        	wordsNum = Constants.easyCorrect.size();
        } else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
        	wordsNum = Constants.normalCorrect.size();
        } else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
        	wordsNum = Constants.hardCorrect.size();
        }
        
        // get common.Constants.itemsPerGame indexes from wordsNum
        lstRandIndex = Randomizer.getRandomIndexes(wordsNum, Constants.itemsPerGame);
        
        lstScore = new ArrayList<>();
        lstWord = new ArrayList<>();
        
        // start game
        game();
    }
	
	
	
	private void game() {
		
		/* if iteration is equal to common.Constants.itemsPerGame
		 * else continue game
		*/

		if(iteration == Constants.itemsPerGame) {	
			// start ResultActivity and finish GameActivity

			Intent intent = new Intent(GameActivity.this, ResultActivity.class);
			
			// send player name
			intent.putExtra(Keys.PLAYER_NAME, this.playerName);

			// send game mode
			intent.putExtra(Keys.GAME_MODE, this.gameMode);

			// send words
			intent.putExtra(Keys.GAME_WORDS, this.lstWord);

			// send scores
			intent.putExtra(Keys.GAME_SCORES, this.lstScore);
			
			startActivity(intent);
			finish();

		} else {
			
			// get the random index
			int index = lstRandIndex.get(iteration);
			
			// set textView of tries left to 3
			textViewTriesLeftVal.setText("3");
			
			// random boolean to randomize options
			Random rand = new Random();
			boolean randBool = rand.nextBoolean();

			if(gameMode.equals(Constants.GAMEMODE_EASY)) {
				
				// get and set the image to display
				try {
				    InputStream ims = getAssets().open(Constants.easyImagesDir 
				    		+ Constants.easyImage.get(index));
				    Drawable d = Drawable.createFromStream(ims, null);
				    imageView.setImageDrawable(d);
				} catch(IOException ex) {}
				
				// set option 1
				textViewOption1.setText(randBool ? Constants.easyCorrect.get(index) : Constants.easyWrong.get(index));

				// set option 2
				textViewOption2.setText(randBool ? Constants.easyWrong.get(index) : Constants.easyCorrect.get(index));
				
				// set up recognizer
				setUpRecognizer(Constants.easyCorrect.get(index), Constants.easyThreshold.get(index));
				
				// set current word
				currWord = Constants.easyCorrect.get(index).toLowerCase();

				// add current word to list of words
				lstWord.add(currWord);
				
			} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
				
				// get and set the image to display
				try {
				    InputStream ims = getAssets().open(Constants.normalImagesDir 
				    		+ Constants.normalImage.get(index));
				    Drawable d = Drawable.createFromStream(ims, null);
				    imageView.setImageDrawable(d);
				} catch(IOException ex) {}
				
				// set option 1
				textViewOption1.setText(randBool ? Constants.normalCorrect.get(index) : Constants.normalWrong.get(index));

				// set option 2
				textViewOption2.setText(randBool ? Constants.normalWrong.get(index) : Constants.normalCorrect.get(index));
				
				// set up recognizer
				setUpRecognizer(Constants.normalCorrect.get(index), Constants.normalThreshold.get(index));
				
				// set current word
				currWord = Constants.normalCorrect.get(index).toLowerCase();

				// add current word to list of words
				lstWord.add(currWord);
				
			} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
				
				// get and set image to display
				try {
				    InputStream ims = getAssets().open(Constants.hardImagesDir 
				    		+ Constants.hardImage.get(index));
				    Drawable d = Drawable.createFromStream(ims, null);
				    imageView.setImageDrawable(d);
				} catch(IOException ex) {}
				
				// set option 1
				textViewOption1.setText(randBool ? Constants.hardCorrect.get(index) : Constants.hardWrong.get(index));
				
				// set option 2
				textViewOption2.setText(randBool ? Constants.hardWrong.get(index) : Constants.hardCorrect.get(index));
				
				// set up recognizer
				setUpRecognizer(Constants.hardCorrect.get(index), Constants.hardThreshold.get(index));
				
				// set current word
				currWord = Constants.hardCorrect.get(index).toLowerCase();

				// add current word to list of words
				lstWord.add(currWord);
				
			}
			
			// set tries to 0
			tries = 0;
			// enable record button
			buttonRecord.setEnabled(true);
		}
		
	}
	
	
	
	/**
	 * set up recognizer
	 * to recognize word 
	 * with Threshold threshold
	 */
	private void setUpRecognizer(String word, float threshold) {	
		// set up recognizer
		recognizer = defaultSetup()
	    		.setAcousticModel(new File(Recognizer.getModelsDir(), "hmm/en-us-semi"))
	      		.setDictionary(new File(Recognizer.getModelsDir(), "dict/cmu07a.dic"))
	      		.setKeywordThreshold(threshold)
	      		.getRecognizer();
		recognizer.addListener(this);
		
		// add keyphrase search for word	
		recognizer.addKeyphraseSearch(Constants.KWS_SEARCH, word.toLowerCase());
	}
	
	
	
	/**
	 * start recording
	 */
	private void startListening() {
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
		String strRecognized = recognizerHypothesis != null 
				? recognizerHypothesis.getHypstr() : "";
		
		// if recognizer did not detect word
		// because of wrong pronunciation
		if(recognizerHypothesis == null) {
			Toast.makeText(getApplicationContext(),
					"Ooops! Wrong Pronunciation", Toast.LENGTH_SHORT)
					.show();
		}
		
		// increment number of tries
		tries++;
		// set tries left textView
		textViewTriesLeftVal.setText("" + (3-tries));
		
		// if recognized
		if(strRecognized.equals(currWord) || strRecognized.contains(currWord)) {
			// compute score
			score = 3-(tries-1);
			// end item with score
			endItem(score);
		} else if(tries == 3) {
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
		// add score to list of scores
		lstScore.add(score);
		// show score
		showItemResultDialog(lstRandIndex.get(iteration), score);
		// add iteration
		iteration++;
		// continue game
		game();
	}
	
	
	
	/**
	 * show alert dialog
	 * with the word and score
	 */
	@SuppressLint("NewApi")
	private void showItemResultDialog(int index, int score) {
		String word="";
		String pronunciation="";
		
		// get the word and pronunciation
		// according to game mode

		if(gameMode.equals(Constants.GAMEMODE_EASY)) {
        	word = Constants.easyCorrect.get(index);
        	pronunciation = Constants.easyPhoneme.get(index);
        } else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
        	word = Constants.normalCorrect.get(index);
        	pronunciation = Constants.normalPhoneme.get(index);
        } else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
        	word = Constants.hardCorrect.get(index);
        	pronunciation = Constants.hardPhoneme.get(index);
        }
		
		// build an alert dialog
		AlertDialog.Builder scoreDialog = new AlertDialog.Builder(this);
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    View dialogView = inflater.inflate(R.layout.iteration_result_layout, null);
	    scoreDialog.setView(dialogView);
	    
	    // word
	    TextView textViewWordVal = (TextView) dialogView.findViewById(R.id.iterationresult_textViewWordVal);
		textViewWordVal.setText(word);
		
		// pronunciation
		TextView textViewPronunciationVal = (TextView) dialogView.findViewById(R.id.iterationresult_textViewPronunciationVal);
		textViewPronunciationVal.setText(pronunciation);
		
		// rating bar
		RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.iterationresult_ratingBar);
		ratingBar.setRating(score);
		ratingBar.setEnabled(false);
		
		final String fWord = word;
		ImageButton buttonPronounce = (ImageButton) dialogView.findViewById(R.id.iterationresult_buttonPronounce);
		buttonPronounce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					MusicManager.pause();
					
					String strWavFile = "pronounce/";
					
					if(gameMode.equals(Constants.GAMEMODE_EASY)) {
						strWavFile += "easy/";
			        } else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
			        	strWavFile += "normal/";
			        } else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
			        	strWavFile += "hard/";
			        }
					
					strWavFile += fWord.toUpperCase() + ".wav";
					
					AssetFileDescriptor afd = getAssets().openFd(strWavFile);
				    MediaPlayer player = new MediaPlayer();
				    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
				    player.prepare();
				    player.start();		
				    
				    while(player.isPlaying());
				    
				    MusicManager.start(GameActivity.this, MusicManager.MUSIC_ALL);
				} catch(Exception e) {
			    }
			}
		});
		
		// show alert dialog
	    AlertDialog myAlert = scoreDialog.create();
	    myAlert.show();
	}
	
	
	
	/**
	 * get partial results from recognizer
	 */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
    	// if hypothesis is not null
    	// set recognizerHypothesis to hypothesis
    	if(hypothesis != null) {
    		recognizerHypothesis = hypothesis;
    	}
    }

    /**
     * get final result from recognizer
	 */
    @Override
    public void onResult(Hypothesis hypothesis) {
    	// if hypothesis is not null
    	// set recognizerHypothesis to hypothesis
    	if(hypothesis != null) {
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
    
    @Override
    public void onBackPressed() {
    	// start MenuActivity and finish GameActivity
    	Intent intent = new Intent(GameActivity.this, MenuActivity.class);
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