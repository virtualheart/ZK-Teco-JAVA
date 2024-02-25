package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.model.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceModel,Integer> {

}
