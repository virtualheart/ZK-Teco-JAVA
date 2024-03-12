package com.gac.employee.attendance.service.impl;

import com.gac.employee.attendance.gateway.DeviceGateway;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.repo.AttendanceRecordRepository;
import com.gac.employee.attendance.repo.EmployeeRepository;
import com.gac.employee.attendance.service.DeviceService;
import com.gac.employee.attendance.service.EmployeeService;
import com.zkteco.Enum.UserRoleEnum;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.commands.UserInfo;
import com.zkteco.terminal.ZKTerminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;
    @Autowired
    private DeviceGateway deviceGateway;
    @Autowired
    private DeviceService deviceService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    ZKTerminal terminal;
    public boolean connectTo() throws IOException {
        try {
            terminal = deviceGateway.getClient();
            if (terminal == null)
                return false;
            return true;
        }catch (DeviceNotConnectException e){
            log.info("Device Not Connect Exception");
        }
        return false;
    }
    @Override
    public List<EmployeeModel> getAllEmployeeFromDB(){
        return empRepo.findAll();
    }

    @Override
    public Optional<EmployeeModel> getEmployeeFromDB(int empId) {
        return empRepo.findById(empId);
    }

    public List<EmployeeModel> getAllEmployeeFromDevice() throws Exception {
        List<UserInfo> userList = terminal.getAllUsers();
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        for (UserInfo userInfo : userList) {
            EmployeeModel employeeModel = mapUserInfoToEmployeeModel(userInfo);
            employeeModelList.add(employeeModel);
        }
        return employeeModelList;
    }

    private EmployeeModel mapUserInfoToEmployeeModel(UserInfo userInfo) {
        EmployeeModel employeeModel = new EmployeeModel();

        employeeModel.setEmployeeId(Integer.parseInt(userInfo.getUserid().trim()));
        employeeModel.setEmployeeName(userInfo.getName().trim());
        employeeModel.setUserRole(com.gac.employee.attendance.enums.UserRole.valueOf(userInfo.getRole().name()));
        employeeModel.setPassword(Integer.parseInt(userInfo.getPassword().trim()));
        employeeModel.setCardNumber(Integer.parseInt(String.valueOf(userInfo.getCardno()).trim()));

        return employeeModel;
    }

    @Override
    public void addUser(EmployeeModel employeeModel) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(employeeModel.getEmployeeName());
        userInfo.setPassword(String.valueOf(employeeModel.getPassword()));
        userInfo.setCardno(employeeModel.getCardNumber());
        userInfo.setRole(UserRoleEnum.USER_DEFAULT);
        userInfo.setUserid(String.valueOf(employeeModel.getEmployeeId()));
        employeeModel.setUserRole(com.gac.employee.attendance.enums.UserRole.USER_DEFAULT);
        try {
            empRepo.save(employeeModel);
            deviceService.connectTo();
            deviceService.addNewEmployee(userInfo);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        } catch (DeviceNotConnectException e) {
            log.info("Device Not Connect Exception");
        }catch (DataIntegrityViolationException s){
            log.warn("Duplicate Record Entry");
        }
        finally {
            deviceService.end();
        }
    }

    public void end() throws IOException {
//        terminal.socketClose();
        if (terminal != null)
            terminal.disconnect();
    }
}
