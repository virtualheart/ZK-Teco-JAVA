package com.gac.employee.attendance.service;

import com.gac.employee.attendance.model.EmployeeModel;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
     boolean connectto() throws IOException;
     List<EmployeeModel> getAllEmployeeFromDB();
     List<EmployeeModel> getAllEmployeeFromDevice() throws Exception;
     void addUser(EmployeeModel employeeModel) throws  IOException;
     void end() throws IOException;
     }
