package com.bill.socialstudy.dataobjects;

import java.util.Calendar;

public class StudySessionObject {

	private String name;
	private int id;
	private Calendar date;
	private int[] students;
	private boolean priv;
	
	public StudySessionObject(int id, String name, Calendar date) {
		super();
		this.name = name;
		this.id = id;
		this.setDate(date);
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

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getDate() {
		return date;
	}

	public void setStudents(int[] students) {
		this.students = students;
	}

	public int[] getStudents() {
		return students;
	}
	
}
