package com.gac.employee.attendance.service;

import com.gac.employee.attendance.model.EmployeeModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
     List<EmployeeModel> getAllEmployeeFromDB();
     Optional<EmployeeModel> getEmployeeFromDB(int empId);

}
