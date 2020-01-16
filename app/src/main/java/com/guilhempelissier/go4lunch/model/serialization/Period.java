package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class Period {
	@Expose
	DateTime open;
	@Expose
	DateTime close;

	public DateTime getOpen() {
		return open;
	}

	public DateTime getClose() {
		return close;
	}
}
