package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "attendance_record")
public class AttendanceRecordModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;
    private int userSN;
    private String userID;
    private AttendanceType verifyType;
    private String recordTime;
    private AttendanceState verifyState;

}
