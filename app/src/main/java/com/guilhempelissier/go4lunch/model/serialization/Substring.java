package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

class Substring {
	@Expose
	private Integer length;
	@Expose
	private Integer offset;

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
