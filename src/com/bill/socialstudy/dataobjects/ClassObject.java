package com.bill.socialstudy.dataobjects;

public class ClassObject {
	private String name;
	private int id;
	private int[] sessions;
	private String instructor;

	public ClassObject(int id, String name, String instructor, Integer[] sessions) {
		super();
		this.name = name;
		this.id = id;
		this.setInstructor(instructor);
		
		for (int i = 0; i < sessions.length; i++) {
			this.sessions[i] = sessions[i];
		}
	}
	
	public ClassObject(int id, String name, String instructor, int[] sessions) {
		super();
		this.name = name;
		this.id = id;
		this.setInstructor(instructor);
		this.sessions = sessions;
	}
	
	public int[] getSessions() {
		return sessions;
	}

	public void setSessions(int[] sessions) {
		this.sessions = sessions;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String toString() {
		return name;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getInstructor() {
		return instructor;
	}
}
