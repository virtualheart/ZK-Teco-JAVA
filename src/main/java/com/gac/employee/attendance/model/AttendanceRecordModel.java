package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "attendance_record")
public class AttendanceRecordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;
    private int userSN;
    private String userID;

    private AttendanceType verifyType;
    @Temporal(TemporalType.DATE)
    private Date recordTime;

    private AttendanceState verifyState;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

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
}
