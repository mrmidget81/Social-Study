package com.bill.socialstudy.dataobjects;

public class CollegeObject {

	private int id;
	private String name;
	private int[] classes;
	
	
	public CollegeObject(int CO_id, String name, Integer[] classIds) {
		id = CO_id;
		this.name = name;
		
		for (int i = 0; i < classIds.length; i++) {
			classes[i] = classIds[i];
		}
	}
	
	public CollegeObject(int CO_id, String name, int[] classIds) {
		id = CO_id;
		this.name = name;
		classes = classIds;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
}
