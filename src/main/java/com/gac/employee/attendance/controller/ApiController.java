package com.gac.employee.attendance.controller;

import com.gac.employee.attendance.component.ExcelGenerator;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.repo.EmployeeRepository;
import com.gac.employee.attendance.service.AttendanceRecordService;
import com.gac.employee.attendance.service.DBservice;
import com.gac.employee.attendance.service.DeviceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    private EmployeeRepository employeeRepository;
    // get methods
    @GetMapping("/lastMonth")
    public ResponseEntity<List<AttendanceRecordModel>> getAttendanceLastMonth() {
        List<AttendanceRecordModel> attendanceRecords = attendanceService.getAttendanceRecordsLastMonth();
        return new ResponseEntity<>(attendanceRecords, HttpStatus.OK);
    }

    @GetMapping("/voiceTest/{voiceIndex}")
    public HttpStatus testVoice(@PathVariable int voiceIndex) throws IOException {
        if (deviceService.connectTo()){
            deviceService.voiceTest(voiceIndex);
            deviceService.end();
            return HttpStatus.OK;
        }
        return HttpStatus.EXPECTATION_FAILED;
    }

    @GetMapping("/export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<AttendanceRecordModel> listOfAttendance = attendanceService.getAttendanceRecordsLastMonth();
        ExcelGenerator generator = new ExcelGenerator(listOfAttendance);
        generator.generateExcelFile(response);
    }

    @GetMapping("/synckusertodb")
    public HttpStatus SynkAllUser() throws IOException, ParseException {
        if (deviceService.connectTo()) {
            List<EmployeeModel> lists = deviceService.getAllEmployeeFromDevice();
            employeeRepository.deleteAll();
            employeeRepository.saveAll(lists);
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
