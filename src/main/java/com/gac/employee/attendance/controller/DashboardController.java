package com.gac.employee.attendance.controller;

import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.service.AttendanceRecordService;
import com.gac.employee.attendance.service.DeviceService;
import com.gac.employee.attendance.service.EmployeeService;
import com.zkteco.Exception.DeviceNotConnectException;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class DashboardController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AttendanceRecordService attendanceRecordService;


//    Dashboard
    @GetMapping({"/","/dashboard"})
    public String dashboard(Model model) throws IOException, ParseException, DeviceNotConnectException{
        deviceService.connectto();
        model.addAttribute("deviceStatus",deviceService.deviceOnlineCheck());
        model.addAttribute("deviceTime",deviceService.GetDeviceTimeDate());
        model.addAttribute("attendanceRecords",deviceService.getAttendanceList());
        model.addAttribute("checkout",attendanceRecordService.getTodayCheckoutCount());
        model.addAttribute("checkin",attendanceRecordService.getTodayCheckinCount());
        deviceService.end();
        return "admin/dashboard";
    }

    //
    @GetMapping("/employee/{empId}")
    public String employeeDetails(Model model){
        model.addAttribute("allemplist", employeeService.getAllEmployeeFromDB());
        return "viewEmployee";
    }

    @GetMapping("/add/user")
    public String addUser(Model model)  {
        EmployeeModel employeeModel = new EmployeeModel();
        model.addAttribute("employeeModel",employeeModel);
        return "admin/addEmployee";
    }

    @GetMapping("/list/user/db")
    public String listEmloyee(Model model){
        model.addAttribute("uri", "admin/list/user/db");
        model.addAttribute("employeeList",employeeService.getAllEmployeeFromDB());
        return "admin/listEmployee";
    }

    @GetMapping("/list/user/device")
    public String addEmployee(Model model) throws Exception {
        employeeService.connectto();
        model.addAttribute("uri", "admin/list/user/device");
        model.addAttribute("employeeList",employeeService.getAllEmployeeFromDevice());
        employeeService.end();
        return "admin/listEmployee";
    }

    @GetMapping("/add/attendance")
    public String addAttendance(){
        return "admin/blank";
    }

    @GetMapping("/attendance/db")
    public String dbAttendance(){
        return "admin/blank";
    }

    @GetMapping("/attendance/device")
    public String deviceAttendance(){
        return "admin/blank";
    }

    // Post methods
    @PostMapping("/synktimedate")
    public String setTime(Model model) throws IOException, DeviceNotConnectException {
        deviceService.connectto();
    	deviceService.synkTimDate();
        deviceService.end();
    	return "redirect:/admin/dashboard";
    }

    @PostMapping("/add/user")
    public String setaddUser(EmployeeModel employeeModel) throws DeviceNotConnectException, IOException, ParseException {
        employeeService.addUser(employeeModel);
        return "redirect:/admin/add/user";
    }
}
