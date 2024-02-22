package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.SmsEnum;

public class SmsInfo {
    private SmsEnum tag;
    private int id;
    private int validMinutes;
    private int reserved;
    private long startTime;

    private String content;

	public SmsEnum getTag() {
		return tag;
	}

	public void setTag(SmsEnum tag) {
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValidMinutes() {
		return validMinutes;
	}

	public void setValidMinutes(int validMinutes) {
		this.validMinutes = validMinutes;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
