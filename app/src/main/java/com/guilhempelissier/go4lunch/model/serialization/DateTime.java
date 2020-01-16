package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class DateTime {
	@Expose
	int day;
	@Expose
	int time;

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}
}
