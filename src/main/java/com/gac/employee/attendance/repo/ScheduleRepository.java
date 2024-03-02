package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.enums.TaskFrequency;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.model.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleModel, Long> {
    Optional<ScheduleModel> findTopByTaskFrequencyAndTaskName(TaskFrequency taskFrequency, String taskName);
}
