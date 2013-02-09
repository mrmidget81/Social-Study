package com.bill.socialstudy.dataobjects;


public class UserObject {

	private int facebookId;
	private int college;
	private String name;
	private int[] classes;
	private int[] sessions;

	public UserObject(int u_id, int college, String name, Integer[] classIds, Integer[] sessionIds) {
		this.facebookId = u_id;
		this.name = name;
		this.college = college;
		
		for (int i = 0; i < sessionIds.length; i++) {
			sessions[i] = sessionIds[i];
		}
		for (int i = 0; i < classIds.length; i++) {
			classes[i] = classIds[i];
		}
	}
	
	public UserObject(int u_id, int college, String name, int[] classIds,
			int[] sessionIds) {
		facebookId = u_id;
		this.name = name;
		this.college = college;
		this.classes = classIds;
		this.sessions = sessionIds;
	}

	public int getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(int facebookId) {
		this.facebookId = facebookId;
	}
	public int getCollege() {
		return college;
	}
	public void setCollege(int college) {
		this.college = college;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getClasses() {
		return classes;
	}
	public void setClasses(int[] classes) {
		this.classes = classes;
	}
	public int[] getSessions() {
		return sessions;
	}
	public void setSessions(int[] sessions) {
		this.sessions = sessions;
	}
	
}
