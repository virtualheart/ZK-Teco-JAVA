package com.gac.employee.attendance.service.impl;

import com.gac.employee.attendance.enums.DeviceSupport;
import com.gac.employee.attendance.gateway.DeviceGateway;
import com.gac.employee.attendance.model.DeviceModel;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.repo.DeviceRepository;
import com.gac.employee.attendance.repo.EmployeeRepository;
import com.gac.employee.attendance.service.DBservice;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.terminal.ZKTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.BindException;
import java.util.List;

@Service
public class DBserviceImpl implements DBservice {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    private static final Logger log = LoggerFactory.getLogger(DeviceGateway.class);
    private ZKTerminal terminal;

    @Override
    public void addNewDevice(DeviceModel deviceModel) throws IOException {

        try {
            log.info("Connecting from DB IP " + " Port " + deviceModel.getIp_address() + " Comm key " + deviceModel.getPort());
            terminal = new ZKTerminal(deviceModel.getIp_address(), deviceModel.getPort());
            terminal.connect();
            terminal.connectAuth(deviceModel.getDevicecommkey());

            deviceModel.setDeviceid(Integer.valueOf(terminal.getDeviceId()));
            deviceModel.setMac(terminal.getMAC());
            deviceModel.setFw_version(String.valueOf(terminal.getFPVersion()));
            deviceModel.setDevicesn(terminal.getSerialNumber());
            deviceModel.setSupport_remote_face_photo(DeviceSupport.UN_SUPPORT);
            deviceModel.setAlias(terminal.getDeviceName());
            deviceModel.setActive(1);
            deviceModel.setPrimaryDevice(1);
            deviceModel.setCompany_id("Gac CA");
            deviceModel.setSupport_remote_finger(DeviceSupport.SUPPORT);
            deviceModel.setSupport_remote_face_photo(DeviceSupport.UN_SUPPORT);
            deviceModel.setSupport_remote_palm_print(DeviceSupport.UN_SUPPORT);
            deviceModel.setEnable(1);
            deviceModel.setType("FP");
            deviceModel.setOEMVendor(terminal.getOEMVendor());
            deviceModel.setFa_version(terminal.getFaceVersion());
            deviceModel.setModel(terminal.getDeviceName());
            deviceModel.setStatus("Active");
            deviceModel.setPlatform(terminal.getPlatform());
            terminal.disconnect();
            deviceRepository.save(deviceModel);

        } catch (DeviceNotConnectException dne) {
            log.warn("Device Not Connect Exception");
        } catch (BindException be){
            log.error("BindException: " + be.getMessage());
            if (be.getMessage().contains("Address already in use")) {
                log.info("Address already in use. Closing the connection.");
                if (terminal != null) {
                    try{
                        terminal.socketClose();
                    } catch (Exception e){
                        log.error("Other BindException: " + e.getMessage());
                    }
                }
            } else {
                log.error("Other BindException: " + be.getMessage());
            }
        }
    }

    @Override
    public List<DeviceModel> getDeviceFromDB() {
        return deviceRepository.findAll();
    }

    @Override
    public void delUserDb(int id) {
        employeeRepository.deleteById(id);
    }
}
