package com.gac.employee.attendance.service;

import com.gac.employee.attendance.model.EmployeeModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
     boolean connectTo() throws IOException;
     List<EmployeeModel> getAllEmployeeFromDB();
     Optional<EmployeeModel> getEmployeeFromDB(int empId);
     List<EmployeeModel> getAllEmployeeFromDevice() throws Exception;
     void addUser(EmployeeModel employeeModel) throws  IOException;
     void end() throws IOException;
     }
