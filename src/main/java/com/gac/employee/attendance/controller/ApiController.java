package com.gac.employee.attendance.controller;

import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.service.AttendanceRecordService;
import com.gac.employee.attendance.service.DBservice;
import com.gac.employee.attendance.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AttendanceRecordService attendanceService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DBservice dBservice;

    // get methods
    @GetMapping("/lastMonth")
    public ResponseEntity<List<AttendanceRecordModel>> getAttendanceLastMonth() {
        List<AttendanceRecordModel> attendanceRecords = attendanceService.getAttendanceRecordsLastMonth();
        return new ResponseEntity<>(attendanceRecords, HttpStatus.OK);
    }

    // post methods
    @GetMapping("/voiceTest/{voiceIndex}")
    public HttpStatus testVoice(@PathVariable int voiceIndex) throws IOException {
        if (deviceService.connectTo()){
            deviceService.voiceTest(voiceIndex);
            deviceService.end();
            return HttpStatus.OK;
        }
        return HttpStatus.EXPECTATION_FAILED;
    }

    // delete methods
    @DeleteMapping("/deleteEmployee/{userId}")
    public HttpStatus delUser(@PathVariable int userId) throws IOException, ParseException {
        if (deviceService.connectTo()) {
            deviceService.delUser(userId);
            deviceService.end();
            return HttpStatus.OK;
        }
        return HttpStatus.EXPECTATION_FAILED;
    }

    @DeleteMapping("/deleteEmployeedb/{userId}")
    public HttpStatus delUserDb(@PathVariable int userId) throws IOException, ParseException {
        dBservice.delUserDb(userId);
        return HttpStatus.OK;
    }

}
