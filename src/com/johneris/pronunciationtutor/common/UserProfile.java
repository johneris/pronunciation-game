package com.johneris.pronunciationtutor.common;

public class UserProfile {

	/* name of user
	*/
	String userName;
	
	/* score in easy mode
	*/
	int easyScore;

	/* score in normal mode
	*/
	int normalScore;

	/* score in hard mode
	*/
	int hardScore;
	
	/* initialize UserProfile
	*/
	public UserProfile() {
		userName = "";
		easyScore = 0;
		normalScore = 0;
		hardScore = 0;
	}
	
	/* initialize UserProfile
	 * with userName, easyScore,
	 * normalScore, and hardScore
	*/
	public UserProfile(String userName, int easyScore,
			int normalScore, int hardScore) {
		this.userName = userName;
		this.easyScore = easyScore;
		this.normalScore = normalScore;
		this.hardScore = hardScore;
	}

	/* get user name
	*/
	public String getUserName() {
		return userName;
	}

	/* set this.userName to userName
	*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/* get easy score
	*/
	public int getEasyScore() {
		return easyScore;
	}

	/* set this.easyScore to easyScore
	*/
	public void setEasyScore(int easyScore) {
		this.easyScore = easyScore;
	}

	/* get normal score
	*/
	public int getNormalScore() {
		return normalScore;
	}

	/* set this.normalScore to normalScore
	*/
	public void setNormalScore(int normalScore) {
		this.normalScore = normalScore;
	}

	/* get hard score
	*/
	public int getHardScore() {
		return hardScore;
	}

	/* set this.hardScore to hardScore
	*/
	public void setHardScore(int hardScore) {
		this.hardScore = hardScore;
	}
	
}
