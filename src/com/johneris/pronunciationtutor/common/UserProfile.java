package com.johneris.pronunciationtutor.common;

import java.util.ArrayList;

public class UserProfile {

	/**
	 * name of user
	 */
	public String userName;
	
	/**
	 * list of results in easy mode
	 */
	public ArrayList<ResultWrapper> lstEasyResult;

	/**
	 * list of results in normal mode
	 */
	public ArrayList<ResultWrapper> lstNormalResult;

	/**
	 * list of results in hard mode
	 */
	public ArrayList<ResultWrapper> lstHardResult;
	
	
	
	/**
	 * initialize UserProfile
	 */
	public UserProfile() {
		userName = "";
		lstEasyResult = new ArrayList<>();
		lstNormalResult = new ArrayList<>();
		lstHardResult = new ArrayList<>();
	}
	
}
