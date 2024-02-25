package com.gac.employee.attendance.service.impl;

import com.gac.employee.attendance.enums.DeviceSupport;
import com.gac.employee.attendance.model.DeviceModel;
import com.gac.employee.attendance.repo.DeviceRepository;
import com.gac.employee.attendance.service.DBservice;
import com.gac.employee.attendance.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class DBserviceImpl implements DBservice {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void addNewDevice(DeviceModel deviceModel) throws IOException {
        if (deviceService.connectTo()){
            deviceModel.setMac(deviceService.getMAC());
            deviceModel.setFw_version(String.valueOf(deviceService.getFPVersion()));
            deviceModel.setDevicesn(deviceService.getSerialNumber());
            deviceModel.setSupport_remote_face_photo(DeviceSupport.UN_SUPPORT);
            deviceModel.setAlias(deviceService.getDeviceName());
            deviceModel.setActive(1);
            deviceModel.setPrimaryDevice(1);
            deviceModel.setCompany_id("Gac CA");
            deviceModel.setSupport_remote_finger(DeviceSupport.SUPPORT);
            deviceModel.setSupport_remote_face_photo(DeviceSupport.UN_SUPPORT);
            deviceModel.setSupport_remote_palm_print(DeviceSupport.UN_SUPPORT);
            deviceModel.setEnable(1);
            deviceModel.setType("FP");
            deviceModel.setOEMVendor(deviceService.getOEMVendor());
            deviceModel.setFa_version(deviceService.getFaceVersion());
            deviceModel.setModel(deviceService.getDeviceName());
            deviceModel.setStatus("Active");
            deviceModel.setPlatform(deviceService.getPlatform());
            deviceService.end();
        }
        deviceRepository.save(deviceModel);
    }

    @Override
    public List<DeviceModel> getDeviceFromDB() {
        return deviceRepository.findAll();
    }
}
