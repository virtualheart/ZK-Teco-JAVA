package com.gac.employee.attendance.controller;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.service.AttendanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AttendanceRecordService attendanceService;

    @GetMapping("/lastMonth")
    public ResponseEntity<List<AttendanceRecordModel>> getAttendanceLastMonth() {
        List<AttendanceRecordModel> attendanceRecords = attendanceService.getAttendanceRecordsLastMonth();
        return new ResponseEntity<>(attendanceRecords, HttpStatus.OK);
    }
}
