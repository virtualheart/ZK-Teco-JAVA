package com.gac.employee.attendance.gateway;

import com.gac.employee.attendance.model.DeviceModel;
import com.gac.employee.attendance.repo.DeviceRepository;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.terminal.ZKTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class DeviceGateway {

    @Autowired
    private final DeviceRepository  deviceRepository;
    private static final Logger log = LoggerFactory.getLogger(DeviceGateway.class);
    private static final String DEFAULT_IP = "192.168.1.205";
    private static final Integer DEFAULT_PORT = 4370;
    private static final Integer DEFAULT_COMM = 0;
    private ZKTerminal terminal;
    public DeviceGateway(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    public ZKTerminal getClient() throws IOException, DeviceNotConnectException {
        if (getIPFromDB() != null && getPortFromDB() != null) {
            try {
                log.info("Connecting from DB IP " + getIPFromDB() + " Port " + getPortFromDB() + " Comm key " + getCommKeyFromDB());
                terminal = new ZKTerminal(getIPFromDB(), getPortFromDB());
                terminal.connect();
                terminal.connectAuth(getCommKeyFromDB());
                return terminal;
            } catch (DeviceNotConnectException dne) {
                log.warn("Device Not Connect Exception");
                log.info("Connecting from DB IP " + DEFAULT_IP + " Port " + DEFAULT_PORT + " Comm key " + DEFAULT_COMM);
                terminal = new ZKTerminal(DEFAULT_IP, DEFAULT_PORT);
                terminal.connect();
                terminal.connectAuth(DEFAULT_COMM);
                return terminal;
            }
        }
        ZKTerminal terminal = new ZKTerminal(DEFAULT_IP, DEFAULT_PORT);
        terminal.connect();
        return terminal;
    }

    public String getIPFromDB(){
        Optional<DeviceModel> device = deviceRepository.findById(1);
        return device.map(DeviceModel::getIp_address).orElse(null);
    }

    public Integer getPortFromDB() {
        Optional<DeviceModel> device = deviceRepository.findById(1);
        return device.isPresent() ? device.get().getPort() : null;
    }

    public Integer getCommKeyFromDB(){
        Optional<DeviceModel> device = deviceRepository.findById(1);
        return device.isPresent() ? device.get().getDevicecommkey() : null;
    }

}
