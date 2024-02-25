package com.gac.employee.attendance.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.SmsInfo;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.commands.UserInfo;

public interface DeviceService {
    boolean connectto() throws IOException ;

    void addNewEmployee(UserInfo userInfo) throws IOException, ParseException, DeviceNotConnectException;
    boolean synkTimDate() throws IOException,DeviceNotConnectException;
    void devicePowerDown() throws DeviceNotConnectException, IOException, ParseException;
    void deviceRestart() throws DeviceNotConnectException, IOException, ParseException;

    String getDeviceTimeDate() throws IOException, ParseException, DeviceNotConnectException;
    boolean deviceOnlineCheck() throws IOException, ParseException, DeviceNotConnectException;
    List<AttendanceRecordModel> getAttendanceList() throws ParseException, DeviceNotConnectException, IOException;
    String getDeviceName() throws IOException;
    Map<String, Integer> getDeviceCapacity() throws IOException;
    String getSerialNumber() throws IOException;
    String getMAC() throws IOException;
    String getFaceVersion() throws IOException;
    String getPlatform() throws IOException;
    boolean getWorkCode() throws IOException;
    int getFPVersion() throws IOException;
    String getOEMVendor() throws IOException;
    List<SmsInfo> getSmsList() throws IOException, ParseException;
    void end() throws IOException;
}
