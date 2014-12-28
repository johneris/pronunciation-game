package com.johneris.pronunciationtutor.common;

public class UserProfile {

	String userName;
	int easyScore;
	int normalScore;
	int hardScore;
	
	public UserProfile() {
		userName = "";
		easyScore = 0;
		normalScore = 0;
		hardScore = 0;
	}
	
	public UserProfile(String userName, int easyScore,
			int normalScore, int hardScore) {
		this.userName = userName;
		this.easyScore = easyScore;
		this.normalScore = normalScore;
		this.hardScore = hardScore;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getEasyScore() {
		return easyScore;
	}

	public void setEasyScore(int easyScore) {
		this.easyScore = easyScore;
	}

	public int getNormalScore() {
		return normalScore;
	}

	public void setNormalScore(int normalScore) {
		this.normalScore = normalScore;
	}

	public int getHardScore() {
		return hardScore;
	}

	public void setHardScore(int hardScore) {
		this.hardScore = hardScore;
	}
	
}
