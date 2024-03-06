package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.enums.TaskFrequency;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.model.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleModel, Long> {
    Optional<ScheduleModel> findTopByTaskName(String taskName);
}
