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
    boolean connectTo() throws IOException ;
    void addNewEmployee(UserInfo userInfo) throws IOException, ParseException, DeviceNotConnectException;
    boolean synkTimDate() throws IOException,DeviceNotConnectException;
    void devicePowerDown() throws DeviceNotConnectException, IOException, ParseException;
    void deviceRestart() throws DeviceNotConnectException, IOException, ParseException;
    String getDeviceTimeDate() ;
    boolean deviceOnlineCheck() throws IOException, ParseException, DeviceNotConnectException;
    List<AttendanceRecordModel> getAttendanceList() throws ParseException, DeviceNotConnectException, IOException;
    List<AttendanceRecordModel> getAttendanceListFromRange(String startDate, String endDate) throws ParseException, DeviceNotConnectException, IOException;
    String getDeviceName() throws IOException;
    Map<String, Integer> getDeviceCapacity() throws IOException;
    String getSerialNumber() throws IOException;
    String getMAC() throws IOException;
    String getFaceVersion() throws IOException;
    String getPlatform() throws IOException;
    boolean getWorkCode() throws IOException;
    int getFPVersion() throws IOException;
    String getDeviceId() throws IOException;
    void delUser(int userId) throws IOException;
    String getOEMVendor() throws IOException;
    List<SmsInfo> getSmsList() throws IOException, ParseException;
    void enrollFinger(int uid, int tempId, String userId) throws IOException;
    void end() throws IOException;
}
