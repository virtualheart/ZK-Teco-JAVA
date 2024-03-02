package com.gac.employee.attendance.dto;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttendanceDTO {
    private int userSN;
    private String userID;
    private AttendanceType verifyType;
    private Date recordTime;
    private AttendanceState verifyState;
}
