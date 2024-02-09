package com.gac.employee.attendance.repo;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecordModel,Integer> {

    List<AttendanceRecordModel> findByRecordTimeAfterAndRecordTimeBeforeAndVerifyState(Date recordTime, Date recordTime2, AttendanceState verifyState);}
