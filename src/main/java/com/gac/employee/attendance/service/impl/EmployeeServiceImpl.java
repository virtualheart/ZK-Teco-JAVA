package com.gac.employee.attendance.service.impl;

import com.gac.employee.attendance.gateway.DeviceGateway;
import com.gac.employee.attendance.model.EmployeeModel;
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

    @Override
    public List<EmployeeModel> getAllEmployeeFromDB(){
        return empRepo.findAll();
    }

    @Override
    public Optional<EmployeeModel> getEmployeeFromDB(int empId) {
        return empRepo.findById(empId);
    }



}
