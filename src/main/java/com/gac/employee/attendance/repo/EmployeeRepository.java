package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.model.AppUserModel;
import com.gac.employee.attendance.model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
    public interface EmployeeRepository extends JpaRepository<EmployeeModel,Integer> {
        Optional<EmployeeModel> findByEmployeeId(int employeeId);

    }

