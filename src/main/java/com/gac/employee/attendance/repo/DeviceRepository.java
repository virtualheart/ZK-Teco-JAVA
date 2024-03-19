package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.DeviceModel;
import com.gac.employee.attendance.model.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceModel,Integer> {
    Optional<DeviceModel> findByPort(int port);

}
