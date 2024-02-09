package com.gac.employee.attendance.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.commands.UserInfo;

public interface DeviceService {
    void addNewEmployee(UserInfo userInfo) throws IOException, ParseException, DeviceNotConnectException;

    boolean synkTimDate() throws IOException,DeviceNotConnectException;
    String GetDeviceTimeDate() throws IOException, ParseException, DeviceNotConnectException;
    boolean deviceOnlineCheck() throws IOException, ParseException, DeviceNotConnectException;
    List<AttendanceRecordModel> getAttendanceList() throws ParseException, DeviceNotConnectException, IOException;
    void end() throws IOException;
    void connectto() throws DeviceNotConnectException, IOException ;

}