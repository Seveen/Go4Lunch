package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

class Term {
	@Expose
	private String value;
	@Expose
	private Integer offset;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
