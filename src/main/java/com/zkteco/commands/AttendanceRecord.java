package com.zkteco.commands;

import java.util.Date;

public class AttendanceRecord {
    private int userSN;
    private String userID;
    private AttendanceType verifyType;
    private Date recordTime;
    private AttendanceState verifyState;

    public AttendanceRecord(int userSN, String userID, AttendanceType verifyType, Date recordTime, AttendanceState verifyState) {
        this.userSN = userSN;
        this.userID = userID;
        this.verifyType = verifyType;
        this.recordTime = recordTime;
        this.verifyState = verifyState;
    }

    // Getter and Setter methods for each field

    public int getUserSN() {
        return userSN;
    }

    public void setUserSN(int userSN) {
        this.userSN = userSN;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public AttendanceType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(AttendanceType verifyType) {
        this.verifyType = verifyType;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public AttendanceState getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(AttendanceState verifyState) {
        this.verifyState = verifyState;
    }

//    @Override
//    public String toString() {
//        return "AttendanceRecord{" +
//                "userSN=" + userSN +
//                ", userID='" + userID + '\'' +
//                ", verifyType=" + verifyType +
//                ", recordTime=" + recordTime +
//                ", verifyState=" + verifyState +
//                '}';
//    }
}
