package com.gac.employee.attendance.enums;

public enum AttendanceType {

    PASSWORD(0),
    FINGERPRINT(1),
    RF_CARD(2);

    private final int attendanceType;

    AttendanceType(int attendanceType){
        this.attendanceType=attendanceType;
    }

    public int getAttendanceType(){
        return attendanceType;
    }
}
