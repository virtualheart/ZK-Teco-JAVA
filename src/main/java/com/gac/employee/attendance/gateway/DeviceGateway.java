package com.gac.employee.attendance.gateway;

import com.gac.employee.attendance.model.DeviceModel;
import com.gac.employee.attendance.repo.DeviceRepository;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.terminal.ZKTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class DeviceGateway {

    @Autowired
    private final DeviceRepository  deviceRepository;
    private static final String DEFAULT_IP = "192.168.1.205";
    private static final Integer DEFAULT_PORT = 4370;
    private ZKTerminal terminal;
    public DeviceGateway(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    public ZKTerminal getClient() throws IOException, DeviceNotConnectException {
        if (getIPFromDB() != null && getPortFromDB() != null) {
            try {
                System.out.println("Connecting from DB IP + " + getIPFromDB() + " Port " + getPortFromDB());
                terminal = new ZKTerminal(getIPFromDB(), getPortFromDB());
                terminal.connect();
                terminal.connectAuth(0);
                return terminal;
            } catch (DeviceNotConnectException dne) {
                System.out.println("Device Not Connect Exception");
                terminal = new ZKTerminal(DEFAULT_IP, DEFAULT_PORT);
                terminal.connect();
                terminal.connectAuth(0);
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

}
