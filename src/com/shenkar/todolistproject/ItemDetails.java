package com.shenkar.todolistproject;
/**
 * This class Represents the items of the list view
 * 
 */

public class ItemDetails  
{
	//Variables
	private String title;
	private int id;
	private int flagDone;
	private int flagAlarm;
	private String time;
	private String date;
	private String description;
	private String location;
	

	public int getFlagAlarm() {
		return flagAlarm;
	}

	public void setFlagAlarm(int flagAlarm) {
		this.flagAlarm = flagAlarm;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getFlagDone() {
		return flagDone;
	}

	public void setFlagDone(int flagDone) {
		this.flagDone = flagDone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

 
	
 
}
