package com.johneris.pronunciationtutor.common;

import java.io.File;

import edu.cmu.pocketsphinx.SpeechRecognizer;

public class Recognizer {
	
	public static File modelsDir;
	
	public static SpeechRecognizer recognizer;
	
	private Recognizer() {
	}
	
	public static void setModelsDir(File m) {
		modelsDir = m;
	}
	
	public static File getModelsDir() {
		return modelsDir;
	}
	
	public static void setSpeechRecognizer(SpeechRecognizer r) {
		recognizer = r;
	}
	
	public static SpeechRecognizer getSpeechRecognizer() {
		return recognizer;
	}
}
