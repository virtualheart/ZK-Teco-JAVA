package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.model.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    @Repository
    public interface EmployeeRepository extends JpaRepository<EmployeeModel,Integer> {

    }

