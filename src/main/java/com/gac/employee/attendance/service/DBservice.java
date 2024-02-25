package com.gac.employee.attendance.service;

import com.gac.employee.attendance.model.DeviceModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface DBservice  {
    void addNewDevice(DeviceModel deviceModel) throws IOException;
    List<DeviceModel> getDeviceFromDB();

}
