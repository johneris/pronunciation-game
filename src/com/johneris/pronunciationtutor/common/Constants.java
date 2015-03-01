package com.johneris.pronunciationtutor.common;

import java.util.ArrayList;

public class Constants {
	
	/**
	 * constant values used for transferring 
	 * data between activities using keys
	 */
	public static final String KEYWORD_SEARCH = "keyword";
	public static final String PHONE_SEARCH = "phones";
	public static final String KWS_SEARCH = "keyphrase";
	
	public static final String GAMEMODE_EASY = "Easy";
	public static final String GAMEMODE_NORMAL = "Normal";
	public static final String GAMEMODE_HARD = "Hard";
	
	public static final String GRADELEVEL_3 = "grade 3";
	public static final String GRADELEVEL_4 = "grade 4";
	public static final String GRADELEVEL_5 = "grade 5";
	
	public static final String GRADELEVEL3_MARKER = "#grade3";
	public static final String GRADELEVEL4_MARKER = "#grade4";
	public static final String GRADELEVEL5_MARKER = "#grade5";
	
	/**
	 * number of words to pronounce
	 * per game instance
	 */
	public static final int itemsPerGame = 10;

	/**
	 * list of word, threshold, image, 
	 * and phoneme per game mode
	 */
	public static ArrayList<String> easyCorrect;
	public static ArrayList<String> easyWrong;
	public static ArrayList<Float> easyThreshold;
	public static ArrayList<String> easyImage;
	public static ArrayList<String> easyPhoneme;
	
	public static ArrayList<String> normalCorrect;
	public static ArrayList<String> normalWrong;
	public static ArrayList<Float> normalThreshold;
	public static ArrayList<String> normalImage;
	public static ArrayList<String> normalPhoneme;
	
	public static ArrayList<String> hardCorrect;
	public static ArrayList<String> hardWrong;
	public static ArrayList<Float> hardThreshold;
	public static ArrayList<String> hardImage;
	public static ArrayList<String> hardPhoneme;
	
	/**
	 * images directory
	 * per game mode
	 */
	public static final String easyImagesDir = "game_images/easy/";
	public static final String normalImagesDir = "game_images/normal/";
	public static final String hardImagesDir = "game_images/hard/";
	
	/**
	 * store scores
	 */
	public static final String userScoreFile = "userScore.txt";
	public static ArrayList<UserProfile> lstUserProfile;
	
	
	static {
		
		lstUserProfile = new ArrayList<>();
		
		easyCorrect = new ArrayList<>();
		easyWrong = new ArrayList<>();
		easyThreshold = new ArrayList<>();
		easyImage = new ArrayList<>();
		easyPhoneme = new ArrayList<>();
		
		easyCorrect.add("BLACK#grade5");				easyWrong.add("BLOCK");		
		easyThreshold.add(1e-20f);				easyImage.add("BLACK - block.jpg");
		easyPhoneme.add("B L AE K");
		
		easyCorrect.add("BLOND#grade5");				easyWrong.add("BLAND");		
		easyThreshold.add(1e-25f);				easyImage.add("BLOND - bland.jpg");
		easyPhoneme.add("B L AA N D");
		
		easyCorrect.add("BOTTLE#grade5");				easyWrong.add("BATTLE");	
		easyThreshold.add(1e-20f);				easyImage.add("BOTTLE - battle.jpg");
		easyPhoneme.add("B AA T AH L");
		
		easyCorrect.add("BOX#grade3");					easyWrong.add("BACKS");		
		easyThreshold.add(1e-10f);				easyImage.add("BOX - BACKS.jpg");
		easyPhoneme.add("B AA K S");
		
		easyCorrect.add("BUG#grade3");					easyWrong.add("BAG");		
		easyThreshold.add(1e-10f);				easyImage.add("BUG - bag.jpg");
		easyPhoneme.add("B AH G");
		
		easyCorrect.add("BUS#grade3");					easyWrong.add("BASS");		
		easyThreshold.add(1e-10f);				easyImage.add("BUS - bass.JPG");
		easyPhoneme.add("B AH S");
		
		easyCorrect.add("CAT#grade3");					easyWrong.add("CUT");		
		easyThreshold.add(1e-5f);				easyImage.add("CAT - cut.jpg");
		easyPhoneme.add("K AE T");
		
		easyCorrect.add("COP#grade3");					easyWrong.add("CAP");		
		easyThreshold.add(1f);					easyImage.add("COP - cap.jpg");
		easyPhoneme.add("K AA P");
		
		easyCorrect.add("DIP#grade4");					easyWrong.add("DEEP");		
		easyThreshold.add(1e-15f);				easyImage.add("DIP - deep.jpg");
		easyPhoneme.add("D IH P");
		
		easyCorrect.add("DRUM#grade4");				easyWrong.add("DRAM");		
		easyThreshold.add(1e-20f);				easyImage.add("DRUM - dram.jpg");
		easyPhoneme.add("D R AH M");
		
		easyCorrect.add("GREEN#grade5");				easyWrong.add("GRIN");		
		easyThreshold.add(1e-15f);				easyImage.add("GREEN - grin.jpg");
		easyPhoneme.add("G R IY N");
		
		easyCorrect.add("HEEL#grade4");				easyWrong.add("HILL");		
		easyThreshold.add(1e-10f);				easyImage.add("HEEL - hill.jpg");
		easyPhoneme.add("HH IY L");
		
		easyCorrect.add("JUG#grade3");					easyWrong.add("JAG");		
		easyThreshold.add(1e-10f);				easyImage.add("JUG - jag.jpg");
		easyPhoneme.add("JH AH G");
		
		easyCorrect.add("LEAVE#grade4");				easyWrong.add("LIVE");		
		easyThreshold.add(1e-15f);				easyImage.add("LEAVE - live.jpg");
		easyPhoneme.add("L IY V");
		
		easyCorrect.add("LOG#grade3");					easyWrong.add("LAG");		
		easyThreshold.add(1e-5f);				easyImage.add("LOG - lag.jpg");
		easyPhoneme.add("L AO G");
		
		easyCorrect.add("MEAT#grade5");				easyWrong.add("MEET");		
		easyThreshold.add(1e-10f);				easyImage.add("MEAT - meet.jpg");
		easyPhoneme.add("M IY T");
		
		easyCorrect.add("MILL#grade5");				easyWrong.add("MEAL");		
		easyThreshold.add(1e-10f);				easyImage.add("MILL - meal.jpg");
		easyPhoneme.add("M IH L");
		
		easyCorrect.add("MOP#grade3");					easyWrong.add("MAP");		
		easyThreshold.add(1e-10f);				easyImage.add("MOP - map.jpg");
		easyPhoneme.add("M AA P");
		
		easyCorrect.add("PEAS#grade3");				easyWrong.add("PEACE");		
		easyThreshold.add(1e-10f);				easyImage.add("PEAS - peace.jpg");
		easyPhoneme.add("P IY Z");
		
		easyCorrect.add("POOL#grade4");				easyWrong.add("PULL");		
		easyThreshold.add(1e-5f);				easyImage.add("POOL - pull.jpg");
		easyPhoneme.add("P UW L");
		
		easyCorrect.add("RAT#grade3");					easyWrong.add("ROT");		
		easyThreshold.add(1e-5f);				easyImage.add("RAT - rot.jpg");
		easyPhoneme.add("R AE T");
		
		easyCorrect.add("RICE#grade4");				easyWrong.add("RISE");		
		easyThreshold.add(1e-10f);				easyImage.add("RICE - rise.jpg");
		easyPhoneme.add("R AY S");
		
		easyCorrect.add("ROCK#grade4");				easyWrong.add("RACK");		
		easyThreshold.add(1e-10f);				easyImage.add("ROCK - rack.jpg");
		easyPhoneme.add("R AA K");
		
		easyCorrect.add("SACK#grade4");				easyWrong.add("SOCK");		
		easyThreshold.add(1e-5f);				easyImage.add("SACK - sock.jpg");
		easyPhoneme.add("S AE K");
		
		easyCorrect.add("SEA#grade4");					easyWrong.add("SEE");		
		easyThreshold.add(1e-10f);				easyImage.add("SEA - see.jpg");
		easyPhoneme.add("S IY");
		
		easyCorrect.add("STEEL#grade5");				easyWrong.add("STEAL");		
		easyThreshold.add(1e-15f);				easyImage.add("STEEL - steal.jpg");
		easyPhoneme.add("S T IY L");
		
		easyCorrect.add("TAIL#grade4");				easyWrong.add("TALE");		
		easyThreshold.add(1e-25f);				easyImage.add("TAIL - tale.jpg");
		easyPhoneme.add("T EY L");
			
		easyCorrect.add("TRUCK#grade5");				easyWrong.add("TRACK");		
		easyThreshold.add(1e-20f);				easyImage.add("TRUCK - track.jpg");
		easyPhoneme.add("T R AH K");
		
		easyCorrect.add("TRUNK#grade5");				easyWrong.add("TRANK");		
		easyThreshold.add(1e-20f);				easyImage.add("TRUNK - trank.png");
		easyPhoneme.add("T R AH NG K");
		
		easyCorrect.add("WITCH#grade5");				easyWrong.add("WHICH");		
		easyThreshold.add(1e-25f);				easyImage.add("WITCH - which.jpg");
		easyPhoneme.add("W IH CH");
		
		
		normalCorrect = new ArrayList<>();
		normalWrong = new ArrayList<>();
		normalThreshold = new ArrayList<>();
		normalImage = new ArrayList<>();
		normalPhoneme = new ArrayList<>();
		
		normalCorrect.add("ANCHOR#grade4");			normalWrong.add("ANSHOR");
		normalThreshold.add(1e-20f);			normalImage.add("ANCHOR_anshor_SilentH.jpg");
		normalPhoneme.add("AE NG K ER");
		
		normalCorrect.add("AUTUMN#grade4"); 			normalWrong.add("AUTUNM");
		normalThreshold.add(1e-20f);			normalImage.add("AUTUMN_autunm_SilentN.jpg");
		normalPhoneme.add("AO T AH M");
		
		normalCorrect.add("BAGUETTE#grade5"); 			normalWrong.add("BAGGUTTE");
		normalThreshold.add(1e-20f);			normalImage.add("BAGUETTE_baggutte_SilentU.jpg");
		normalPhoneme.add("B AE G EH T");
		
		normalCorrect.add("CASTLE#grade4"); 			normalWrong.add("CASTEL");
		normalThreshold.add(1e-15f);			normalImage.add("CASTLE_castel_SilentT.jpg");
		normalPhoneme.add("K AE S AH L");
		
		normalCorrect.add("CHAMPAGNE#grade5"); 		normalWrong.add("CHAMPAGEN");
		normalThreshold.add(1e-20f);			normalImage.add("CHAMPAGNE_champagen.jpg");
		normalPhoneme.add("SH AE M P EY N");
		
		normalCorrect.add("CHOIR#grade4"); 			normalWrong.add("CHIOR");
		normalThreshold.add(1e-10f);			normalImage.add("CHOIR_chior_SilentH.gif");
		normalPhoneme.add("K W AY ER");
		
		normalCorrect.add("FEIGN#grade3"); 			normalWrong.add("FIEGN");
		normalThreshold.add(1f);				normalImage.add("FEIGN_fiegn_SilentG.jpg");
		normalPhoneme.add("F EY N");
		
		normalCorrect.add("FLUORESCENT#grade5"); 		normalWrong.add("FLEWRESCENT");
		normalThreshold.add(1e-35f);			normalImage.add("FLUORESCENT_flewrescent_SilentC.jpg");
		normalPhoneme.add("F L UH R EH S AH N T");
		
		normalCorrect.add("GNARL#grade3"); 			normalWrong.add("GARNL");
		normalThreshold.add(1e-10f);				normalImage.add("GNARL_garnl_SilentG.jpg");
		normalPhoneme.add("N AA R L");
		
		normalCorrect.add("GNAW#grade3"); 				normalWrong.add("GAWN");
		normalThreshold.add(1e-5f);				normalImage.add("GNAW_gawn_SilentG.jpg");
		normalPhoneme.add("N AO");
		
		normalCorrect.add("GNOME#grade3"); 			normalWrong.add("GNOEM");
		normalThreshold.add(1e-10f);			normalImage.add("GNOME_gnoem_SilentG.jpg");
		normalPhoneme.add("N OW M");
		
		normalCorrect.add("GUITAR#grade3"); 			normalWrong.add("GIUTAR");
		normalThreshold.add(1e-20f);			normalImage.add("GUITAR_giutar_SilentU.jpg");
		normalPhoneme.add("G IH T AA R");
		
		normalCorrect.add("HANDKERCHIEF#grade5"); 		normalWrong.add("HANDKERCHEAP");
		normalThreshold.add(1e-20f);			normalImage.add("HANDKERCHIEF_handkercheap_SilentD.jpg");
		normalPhoneme.add("HH AE NG K ER CH IH F");
		
		normalCorrect.add("JOSTLE#grade4"); 			normalWrong.add("JOSLET");
		normalThreshold.add(1e-20f);			normalImage.add("JOSTLE_joslet_SilentT.jpg");
		normalPhoneme.add("JH AA S AH L");
		
		normalCorrect.add("KNEAD#grade3"); 			normalWrong.add("KEAND");
		normalThreshold.add(1e-10f);			normalImage.add("KNEAD_keand_SilentK.jpg");
		normalPhoneme.add("N IY D");
		
		normalCorrect.add("KNOB#grade3"); 				normalWrong.add("KNOUB");
		normalThreshold.add(1e-5f);				normalImage.add("KNOB_knoub_SilentK.jpg");
		normalPhoneme.add("N AA B");
		
		normalCorrect.add("LAMB#grade3"); 				normalWrong.add("LAMV");
		normalThreshold.add(1e-5f);				normalImage.add("LAMB_lamv_SilentB.jpg");
		normalPhoneme.add("L AE M");
		
		normalCorrect.add("LEPRECHAUN#grade5"); 		normalWrong.add("LEPRECHAIN");
		normalThreshold.add(1e-20f);			normalImage.add("LEPRECHAUN_leprechain_SilentH.jpg");
		normalPhoneme.add("L EH P R IH K AA N");
		
		normalCorrect.add("LOCH#grade3"); 				normalWrong.add("LOHC");
		normalThreshold.add(1e-10f);			normalImage.add("LOCH_lohc_SilentH.jpg");
		normalPhoneme.add("L AA K");
		
		normalCorrect.add("MELANCHOLY#grade5"); 		normalWrong.add("MELANSHOLY");
		normalThreshold.add(1e-25f);			normalImage.add("MELANCHOLY_melansholy_SilentH.jpg");
		normalPhoneme.add("M EH L AH N K AA L IY");
		
		normalCorrect.add("MORTGAGE#grade5"); 			normalWrong.add("MORGAGE");
		normalThreshold.add(1e-25f);			normalImage.add("MORTGAGE_morgage_SilentT.jpg");
		normalPhoneme.add("M AO R G AH JH");
		
		normalCorrect.add("PSYCHIATRIST#grade5");		normalWrong.add("PSYCHATRIST");
		normalThreshold.add(1e-35f);			normalImage.add("PSYCHIATRIST_psychatrist_SilentP.jpg");
		normalPhoneme.add("S AH K AY AH T R AH S T");
		
		normalCorrect.add("RECEIPT#grade4"); 			normalWrong.add("RECEIP");
		normalThreshold.add(1e-20f);			normalImage.add("RECEIPT_receip_SilentP.jpg");
		normalPhoneme.add("R IH S IY T");
		
		normalCorrect.add("SILHOUETTE#grade5"); 		normalWrong.add("SILLUETTE");
		normalThreshold.add(1e-25f);			normalImage.add("SILHOUETTE_silluette_SilentU.jpg");
		normalPhoneme.add("S IH L AH W EH T");
		
		normalCorrect.add("SWORD#grade3"); 			normalWrong.add("SOWRD");
		normalThreshold.add(1e-20f);			normalImage.add("SWORD_sowrd_SilentW.jpeg");
		normalPhoneme.add("S AO R D");
		
		normalCorrect.add("THUMB#grade4"); 			normalWrong.add("THAMV");
		normalThreshold.add(1e-10f);			normalImage.add("THUMB_thamv.jpg");
		normalPhoneme.add("TH AH M");
		
		normalCorrect.add("WHISTLE#grade4"); 			normalWrong.add("WHISLET");
		normalThreshold.add(1e-25f);			normalImage.add("WHISTLE_whislet_SilentT.png");
		normalPhoneme.add("W IH S AH L");
		
		normalCorrect.add("WRENCH#grade4"); 			normalWrong.add("WERNCH");
		normalThreshold.add(1e-25f);			normalImage.add("WRENCH_wernch_SilentW.jpg");
		normalPhoneme.add("R EH N CH");
		
		normalCorrect.add("WRIST#grade4");				normalWrong.add("WRITS");
		normalThreshold.add(1e-25f);			normalImage.add("Wrist_writs_SilentW.gif");
		normalPhoneme.add("R IH S T");
		
		normalCorrect.add("CYMBAL#grade5"); 			normalWrong.add("SYMBOL");
		normalThreshold.add(1e-20f);			normalImage.add("cymbal_symbol.jpg");
		normalPhoneme.add("S IH M B AH L");
		
		
		hardCorrect = new ArrayList<>();
		hardWrong = new ArrayList<>();
		hardThreshold = new ArrayList<>();
		hardImage = new ArrayList<>();
		hardPhoneme = new ArrayList<>();
		
		hardCorrect.add("ACQUIESCE#grade3");			hardWrong.add("ACQUISCE");
		hardThreshold.add(1e-25f);				hardImage.add("ACQUIESCE - acquisce.jpg");
		hardPhoneme.add("AE K W IY EH S");
		
		hardCorrect.add("BACCALAUREATE#grade4");		hardWrong.add("BACCALAORETTE");
		hardThreshold.add(1e-35f);				hardImage.add("BACCALAUREATE - baccalaorette.jpg");
		hardPhoneme.add("B AE K AH L AO R IY AH T");
		
		hardCorrect.add("BOUQUET#grade3");				hardWrong.add("BOUQUETE");
		hardThreshold.add(1e-20f);				hardImage.add("BOUQUET - bouquete.jpg");
		hardPhoneme.add("B UW K EY");
		
		hardCorrect.add("BRUISE#grade3");				hardWrong.add("BRUOISE");
		hardThreshold.add(1e-20f);				hardImage.add("BRUISE - bruoise.jpg");
		hardPhoneme.add("B R UW Z");
		
		hardCorrect.add("CAMOUFLAGE#grade3");			hardWrong.add("CAMUOFLAGE");
		hardThreshold.add(1e-30f);				hardImage.add("CAMOUFLAGE - camuoflage.jpg");
		hardPhoneme.add("K AE M AH F L AA ZH");
		
		hardCorrect.add("CHANDELIER#grade3");			hardWrong.add("CHANDELLIER");
		hardThreshold.add(1e-30f);				hardImage.add("CHANDELIER - chandellier.jpg");
		hardPhoneme.add("SH AE N D AH L IH R");
		
		hardCorrect.add("COALESCENCE#grade4");			hardWrong.add("COLLESCENCE");
		hardThreshold.add(1e-35f);				hardImage.add("COALESCENCE - collescence.jpg");
		hardPhoneme.add("K OW AH L EH S AH N S");
		
		hardCorrect.add("EAVESDROP#grade4");			hardWrong.add("EEVESDROP");
		hardThreshold.add(1e-30f);				hardImage.add("EAVESDROP - eevesdrop.jpg");
		hardPhoneme.add("IY V Z D R AA P");
		
		hardCorrect.add("ELUCUBRATE#grade3");			hardWrong.add("ELUKUBRAIT");
		hardThreshold.add(1e-35f);				hardImage.add("ELUCUBRATE - elukubrait.jpg");
		hardPhoneme.add("IH L UW K Y UW B R EY T");
		
		hardCorrect.add("ESQUAMULOSE#grade4");			hardWrong.add("ESQUAMMULOSE");
		hardThreshold.add(1e-35f);				hardImage.add("ESQUAMULOSE - esquammulose.jpg");
		hardPhoneme.add("EH S K W AA M Y UW L OW S");
		
		hardCorrect.add("GAZETTEER#grade4");			hardWrong.add("GAZETEER");
		hardThreshold.add(1e-35f);				hardImage.add("GAZETTEER - gazeteer.jpg");
		hardPhoneme.add("G AH Z IY T IH R");
		
		hardCorrect.add("GYMKHANA#grade4");			hardWrong.add("GYMKANA");
		hardThreshold.add(1e-35f);				hardImage.add("GYMKHANA - gymkana.jpg");
		hardPhoneme.add("JH IH M K AE N AH");
		
		hardCorrect.add("HERBACEOUS#grade5");			hardWrong.add("HERBACIOUS");
		hardThreshold.add(1e-20f);				hardImage.add("HERBACEOUS - herbacious.jpg");
		hardPhoneme.add("ER B EY SH AH S");
		
		hardCorrect.add("LACKADAISICAL#grade5");		hardWrong.add("LACKADYSICAL");
		hardThreshold.add(1e-35f);				hardImage.add("LACKADAISICAL - lackadysical.jpg");
		hardPhoneme.add("L AE K AH D EY Z IH K AH L");
		
		hardCorrect.add("MARAUDER#grade3");			hardWrong.add("MAROUDER");
		hardThreshold.add(1e-20f);				hardImage.add("MARAUDER - marouder.jpg");
		hardPhoneme.add("M ER AO D ER");
		
		hardCorrect.add("MOUSTACHE#grade4");			hardWrong.add("MOUSTTACHE");
		hardThreshold.add(1e-20f);				hardImage.add("MOUSTACHE - mousttache.jpg");
		hardPhoneme.add("M AH S T AE SH");
		
		hardCorrect.add("ORFEVRERIE#grade5");			hardWrong.add("ORFEVRERRIE");
		hardThreshold.add(1e-35f);				hardImage.add("ORFEVRERIE - orfevrerrie.jpg");
		hardPhoneme.add("AO P IH V R EH R IY");
		
		hardCorrect.add("PARAPHERNALIA#grade5");		hardWrong.add("PHARAPERNALIA");
		hardThreshold.add(1e-30f);				hardImage.add("PARAPHERNALIA - pharapernalia.jpg");
		hardPhoneme.add("P EH R AH F AH N EY L Y AH");
		
		hardCorrect.add("PHYSICS#grade3");				hardWrong.add("PHYSIQUES");
		hardThreshold.add(1e-20f);				hardImage.add("PHYSICS - physiques.jpg");
		hardPhoneme.add("F IH Z IH K S");
		
		hardCorrect.add("PHYSIQUE#grade3");			hardWrong.add("PHYSICUE");
		hardThreshold.add(1e-20f);				hardImage.add("PHYSIQUE - physicue.jpg");
		hardPhoneme.add("F AH Z IY K");
		
		hardCorrect.add("PICTURESQUE#grade4");			hardWrong.add("PICTURESQUEUE");
		hardThreshold.add(1e-25f);				hardImage.add("PICTURESQUE - picturesqueue.jpg");
		hardPhoneme.add("P IH K CH ER AH S K");
		
		hardCorrect.add("PIROUETTE#grade4");			hardWrong.add("PIRROUETTE");
		hardThreshold.add(1e-20f);				hardImage.add("PIROUETTE - pirrouette.jpg");
		hardPhoneme.add("P IH R UW EH");
		
		hardCorrect.add("PLEBEIAN#grade4");			hardWrong.add("PLEBBEIAN");
		hardThreshold.add(1e-25f);				hardImage.add("PLEBEIAN - plebbeian.jpg");
		hardPhoneme.add("P L AH B IY AH N");
		
		hardCorrect.add("POLTERGEIST#grade5");			hardWrong.add("POLTERGIEST");
		hardThreshold.add(1e-30f);				hardImage.add("POLTERGEIST - poltergiest.jpg");
		hardPhoneme.add("P OW L T ER G AY S T");
		
		hardCorrect.add("PSITTACINE#grade5");			hardWrong.add("PSYTTACINE");
		hardThreshold.add(1e-35f);				hardImage.add("PSITTACINE - psyttacine.jpg");
		hardPhoneme.add("S IH T AH S AY N");
		
		hardCorrect.add("QUEUE#grade3");				hardWrong.add("QUEEUE");
		hardThreshold.add(1e-10f);				hardImage.add("QUEUE - queeue.jpg");
		hardPhoneme.add("K Y UW");
		
		hardCorrect.add("REMUNERATION#grade5");		hardWrong.add("REMMUNERATION");
		hardThreshold.add(1e-35f);				hardImage.add("REMUNERATION - remmuneration.jpg");
		hardPhoneme.add("R IH M Y UW N ER EY SH AH N");
		
		hardCorrect.add("SILHOUETTE#grade5");			hardWrong.add("SILOUETTE");
		hardThreshold.add(1e-25f);				hardImage.add("SILHOUETTE - silouette.jpg");
		hardPhoneme.add("S IH L AH W EH T");
		
		hardCorrect.add("SMARAGDINE#grade5");			hardWrong.add("SMARUGDINE");
		hardThreshold.add(1e-35f);				hardImage.add("SMARAGDINE - smarugdine.jpg");
		hardPhoneme.add("Z M AH R AE G D IY N");
		
		hardCorrect.add("STRAITJACKET#grade5");		hardWrong.add("STRAIGHTJACKET");
		hardThreshold.add(1e-30f);				hardImage.add("STRAITJACKET - straightjacket.jpg");
		hardPhoneme.add("S T R EY T JH AE K AH T");
		
	}
	
}
