package com.gac.employee.attendance.service;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.zkteco.Exception.DeviceNotConnectException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface AttendanceRecordService {

    List<AttendanceRecordModel> getAttendanceFormDB();
    long getTodayCheckinCount();
    long getTodayCheckoutCount();
    public List<AttendanceRecordModel> getAttendanceRecordsLastMonth();

}
