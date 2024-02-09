package com.zkteco.commands;

//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.charset.StandardCharsets;
import java.util.Date;

//import com.zkteco.utils.HexUtils;

public class SmsInfo {
    public int getTag() {
		return tag;
	}
	public void setTag(short tag) {
		this.tag = tag;
	}
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public long getValidMinutes() {
		return validMinutes;
	}
	public void setValidMinutes(int validMinutes) {
		this.validMinutes = validMinutes;
	}
	public long getReserved() {
		return reserved;
	}
	public void setReserved(int reserved) {
		this.reserved = reserved;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	    
    
    public SmsInfo(int tag, int ID, Date startTime, long reserved, long validMinutes,
			String content) {
	
        this.tag = (short)tag;
        this.id = ID;
        this.validMinutes = validMinutes;
        this.reserved = (int)reserved;
        this.startTime = startTime;
        this.content = content;        
    }
    	
	private short tag;
    private int id;
    private long validMinutes;
    private int reserved;
    private Date startTime;
    private String content;
}
