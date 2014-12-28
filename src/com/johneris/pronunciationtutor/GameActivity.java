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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
//import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

public class GameActivity extends Activity implements
        RecognitionListener {
	
	boolean continueMusic = true;
	
	TextView textViewNameVal;
	TextView textViewGameModeVal;
	
	Button buttonSkip;
	Button buttonRecord;
	
	TextView textViewTriesLeftVal;
	
	ImageView imageView;
	
	TextView textViewOption1;
	TextView textViewOption2;
	
	SpeechRecognizer recognizer;
	Hypothesis recognizerHypothesis;
	boolean isListening;
	
	String gameMode;
	String playerName;
	
	int iteration;
	int tries;
	String currWord;
	
	ArrayList<Integer> lstRandIndex;
	ArrayList<Integer> lstScore;
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
        	playerName = extras.containsKey(Keys.PLAYER_NAME) ? 
        		extras.getString(Keys.PLAYER_NAME) : "";
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
					stopListening();
					buttonSkip.setEnabled(true);
				} else {
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
        recognizer = Recognizer.getSpeechRecognizer();
        iteration = 0;
        
        int wordsNum = 0;
        
        if(gameMode.equals(Constants.GAMEMODE_EASY)) {
        	wordsNum = Constants.easyCorrect.size();
        } else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
        	wordsNum = Constants.normalCorrect.size();
        } else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
        	wordsNum = Constants.hardCorrect.size();
        }
        
        lstRandIndex = Randomizer.getRandomIndexes(wordsNum, Constants.itemsPerGame);
        
        lstScore = new ArrayList<>();
        lstWord = new ArrayList<>();
        
        // start game
        game();
    }
	
	
	
	private void game() {
		
		if(iteration == Constants.itemsPerGame) {
			
			Intent intent = new Intent(GameActivity.this, ResultActivity.class);
			
			intent.putExtra(Keys.PLAYER_NAME, this.playerName);
			intent.putExtra(Keys.GAME_MODE, this.gameMode);
			intent.putExtra(Keys.GAME_WORDS, this.lstWord);
			intent.putExtra(Keys.GAME_SCORES, this.lstScore);
			
			startActivity(intent);
			finish();
			
		} else {
			
			int index = lstRandIndex.get(iteration);
			
			textViewTriesLeftVal.setText("3");
			
			Random rand = new Random();
			boolean randBool = rand.nextBoolean();
			
			if(gameMode.equals(Constants.GAMEMODE_EASY)) {
				
				try {
				    InputStream ims = getAssets().open(Constants.easyImagesDir 
				    		+ Constants.easyImage.get(index));
				    Drawable d = Drawable.createFromStream(ims, null);
				    imageView.setImageDrawable(d);
				} catch(IOException ex) {}
				
				textViewOption1.setText(randBool ? Constants.easyCorrect.get(index) : Constants.easyWrong.get(index));
				textViewOption2.setText(randBool ? Constants.easyWrong.get(index) : Constants.easyCorrect.get(index));
				
				setUpRecognizer(Constants.easyCorrect.get(index), Constants.easyThreshold.get(index));
				
				currWord = Constants.easyCorrect.get(index).toLowerCase();
				lstWord.add(currWord);
				
			} else if(gameMode.equals(Constants.GAMEMODE_NORMAL)) {
				
				try {
				    InputStream ims = getAssets().open(Constants.normalImagesDir 
				    		+ Constants.normalImage.get(index));
				    Drawable d = Drawable.createFromStream(ims, null);
				    imageView.setImageDrawable(d);
				} catch(IOException ex) {}
				
				textViewOption1.setText(randBool ? Constants.normalCorrect.get(index) : Constants.normalWrong.get(index));
				textViewOption2.setText(randBool ? Constants.normalWrong.get(index) : Constants.normalCorrect.get(index));
				
				setUpRecognizer(Constants.normalCorrect.get(index), Constants.normalThreshold.get(index));
				
				currWord = Constants.normalCorrect.get(index).toLowerCase();
				lstWord.add(currWord);
				
			} else if(gameMode.equals(Constants.GAMEMODE_HARD)) {
				
				try {
				    InputStream ims = getAssets().open(Constants.hardImagesDir 
				    		+ Constants.hardImage.get(index));
				    Drawable d = Drawable.createFromStream(ims, null);
				    imageView.setImageDrawable(d);
				} catch(IOException ex) {}
				
				textViewOption1.setText(randBool ? Constants.hardCorrect.get(index) : Constants.hardWrong.get(index));
				textViewOption2.setText(randBool ? Constants.hardWrong.get(index) : Constants.hardCorrect.get(index));
				
				setUpRecognizer(Constants.hardCorrect.get(index), Constants.hardThreshold.get(index));
				
				currWord = Constants.hardCorrect.get(index).toLowerCase();
				lstWord.add(currWord);
				
			}
			
			tries = 0;
			buttonRecord.setEnabled(true);
		}
		
	}
	
	
	
	private void setUpRecognizer(String word, float threshold) {	
		recognizer = defaultSetup()
	    		.setAcousticModel(new File(Recognizer.getModelsDir(), "hmm/en-us-semi"))
	      		.setDictionary(new File(Recognizer.getModelsDir(), "dict/cmu07a.dic"))
	      		.setKeywordThreshold(threshold)
	      		.getRecognizer();
		recognizer.addListener(this);
			
		recognizer.addKeyphraseSearch(Constants.KWS_SEARCH, word.toLowerCase());
	}
	
	
	
	private void startListening() {
		isListening = true;
		recognizerHypothesis = null;
		
		MusicManager.pause();
		
		recognizer.startListening(Constants.KWS_SEARCH);
		
		buttonRecord.setText("Stop");
	}
	
	
	
	private void stopListening() {
		int score = 0;
		isListening = false;
		
		recognizer.stop();
		
		String strRecognized = recognizerHypothesis != null 
				? recognizerHypothesis.getHypstr() : "";
				
		if(recognizerHypothesis == null) {
			Toast.makeText(getApplicationContext(),
					"Ooops! Wrong Pronunciation", Toast.LENGTH_SHORT)
					.show();
		}
		
		tries++;
		textViewTriesLeftVal.setText("" + (3-tries));
		
		if(strRecognized.equals(currWord) || strRecognized.contains(currWord)) {
			score = 3-(tries-1);
			endItem(score);
		} else if(tries == 3) {
			score = 0;
			endItem(score);
		}
		
		buttonRecord.setText("Record");
		MusicManager.start(this, MusicManager.MUSIC_ALL);
	}
	
	
	
	private void endItem(int score) {
		lstScore.add(score);
		showItemResultDialog(lstRandIndex.get(iteration), score);
		iteration++;
		game();
	}
	
	
	
	@SuppressLint("NewApi")
	private void showItemResultDialog(int index, int score) {
		String word="", pronunciation="";
		
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
		
		AlertDialog.Builder scoreDialog = new AlertDialog.Builder(this);
	    LayoutInflater inflater = this.getLayoutInflater();
	    
	    View dialogView = inflater.inflate(R.layout.iteration_result_layout, null);
	    scoreDialog.setView(dialogView);
	    
	    TextView textViewWordVal = (TextView) dialogView.findViewById(R.id.iterationresult_textViewWordVal);
		textViewWordVal.setText(word);
		
		TextView textViewPronunciationVal = (TextView) dialogView.findViewById(R.id.iterationresult_textViewPronunciationVal);
		textViewPronunciationVal.setText(pronunciation);
		
		RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.iterationresult_ratingBar);
		ratingBar.setRating(score);
		ratingBar.setEnabled(false);
		
	    AlertDialog myAlert = scoreDialog.create();
	    myAlert.show();
	}
	
	
	
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
    	if(hypothesis != null) {
    		recognizerHypothesis = hypothesis;
    	}
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
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
    	// promt user lost data
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