package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class DateTime {
	@Expose
	int day;
	@Expose
	int time;

	public DateTime(int day, int time) {
		this.day = day;
		this.time = time;
	}

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}
}
