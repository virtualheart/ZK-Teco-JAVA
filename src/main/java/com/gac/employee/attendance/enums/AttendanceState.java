package com.gac.employee.attendance.enums;

import lombok.Getter;

@Getter
public enum AttendanceState {
    CHECK_IN(0),
    CHECK_OUT(1),
    BREAK_IN(2),
    BREAK_OUT(3),
    OT_IN(4),
    OT_OUT(5);

    private final int attendanceState;

    AttendanceState(int attendanceState){
        this.attendanceState=attendanceState;
    }

}
