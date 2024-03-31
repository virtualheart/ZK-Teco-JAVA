package com.gac.employee.attendance.controller;

import com.gac.employee.attendance.dto.AttendanceDTO;
import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.DeviceModel;
import com.gac.employee.attendance.model.EmployeeModel;
import com.gac.employee.attendance.repo.AttendanceRecordRepository;
import com.gac.employee.attendance.service.AttendanceRecordService;
import com.gac.employee.attendance.service.DBservice;
import com.gac.employee.attendance.service.DeviceService;
import com.gac.employee.attendance.service.EmployeeService;
import com.zkteco.Exception.DeviceNotConnectException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin")
public class DashboardController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private DBservice dBservice;


    //    Dashboard
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) throws IOException, ParseException, DeviceNotConnectException {
        if (deviceService.connectTo()) {
            model.addAttribute("deviceStatus", deviceService.deviceOnlineCheck());
            model.addAttribute("deviceTime", deviceService.getDeviceTimeDate());
            model.addAttribute("attendanceRecords", deviceService.getAttendanceList());
            deviceService.end();
        } else {
            model.addAttribute("deviceStatus", deviceService.deviceOnlineCheck());
            model.addAttribute("deviceTime", deviceService.getDeviceTimeDate());
            model.addAttribute("attendanceRecords", null);

        }

        model.addAttribute("checkout", attendanceRecordService.getTodayCheckoutCount());
        model.addAttribute("checkin", attendanceRecordService.getTodayCheckinCount());

        return "admin/dashboard";
    }

    //
    @GetMapping("/employee/{empId}")
    public String employeeDetails(@PathVariable int empId, Model model) {
        model.addAttribute("allemplist", employeeService.getEmployeeFromDB(empId));
        return "admin/listEmployeeDb";
    }

    @GetMapping("/add/user")
    public String addUser(Model model) {
        EmployeeModel employeeModel = new EmployeeModel();
        model.addAttribute("employeeModel", employeeModel);
        return "admin/addEmployee";
    }

    @GetMapping("/list/user/db")
    public String listEmloyee(Model model) {
        model.addAttribute("uri", "admin/list/user/db");
        model.addAttribute("employeeList", employeeService.getAllEmployeeFromDB());
        return "admin/listEmployeeDb";
    }

    @GetMapping("/list/device/sms")
    public String smsList(Model model) throws IOException, ParseException {
        if (deviceService.connectTo()) {
            model.addAttribute("smslists", deviceService.getSmsList());
        }
        deviceService.end();
        return "admin/smsList";
    }

    @GetMapping("/list/user/device")
    public String addEmployee(Model model) throws Exception {
        if (deviceService.connectTo()) {
            model.addAttribute("employeeList", deviceService.getAllEmployeeFromDevice());
            deviceService.end();
        } else {
            model.addAttribute("employeeList", null);
        }
        return "admin/listEmployeeDev";
    }

    @GetMapping("/add/attendance")
    public String addAttendance(Model model) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        model.addAttribute("AttendanceDTO",attendanceDTO);
        model.addAttribute("Users",employeeService.getAllEmployeeFromDB());
        return "admin/add_Attendance";
    }

    @GetMapping("/attendance/db")
    public String dbAttendance(Model model) {
        model.addAttribute("attendanceRecords", attendanceRecordService.getAttendanceFormDB());
        return "admin/attendanceDb";
    }

    @GetMapping("/attendance/device")
    public String deviceAttendance(Model model) throws IOException, DeviceNotConnectException, ParseException {
        if (deviceService.connectTo()) {
            model.addAttribute("attendanceRecords", deviceService.getAttendanceList());
            deviceService.end();
        }
        return "admin/attendanceDev";
    }

    @GetMapping("/device/new")
    public String addDevice(Model model) {
        DeviceModel deviceModel = new DeviceModel();
        model.addAttribute("deviceModel", deviceModel);
        return "admin/addDevice";
    }

    @GetMapping("/device/smslist")
    public String deviceSMSlist() {
        return "admin/smsList";
    }

    @GetMapping("/list/Devices")
    public String devicelist(Model model) {
        model.addAttribute("devices", dBservice.getDeviceFromDB());
        return "admin/deviceList";
    }

    @GetMapping("/settings/device")
    public String dangerPage(Model model) {
        return "admin/dangerPage";
    }

    @GetMapping("/device/Capacity")
    public String deviceCapacity(Model model) throws IOException {
        if (deviceService.connectTo()) {
            Map<String, String> userFriendlyKeys = Map.ofEntries(
                    Map.entry("adminCount", "Admin Count"),
                    Map.entry("userCount", "User Count"),
                    Map.entry("fpCount", "Fingerprint Count"),
                    Map.entry("pwdCount", "Password Count"),
                    Map.entry("oplogCount", "Operation Log Count"),
                    Map.entry("attlogCount", "Attendance Log Count"),
                    Map.entry("fpCapacity", "Fingerprint Capacity"),
                    Map.entry("userCapacity", "User Capacity"),
                    Map.entry("attlogCapacity", "Attendance Log Capacity"),
                    Map.entry("remainingFp", "Remaining Fingerprints"),
                    Map.entry("remainingUser", "Remaining Users"),
                    Map.entry("remainingAttlog", "Remaining Attendance Logs"),
                    Map.entry("faceCount", "Face Count"),
                    Map.entry("faceCapacity", "Face Capacity")
            );

            Map<String, Integer> transformedDeviceCapacity = new LinkedHashMap<>();
            deviceService.getDeviceCapacity().forEach((key, value) -> {
                String userFriendlyKey = userFriendlyKeys.getOrDefault(key, key);
                transformedDeviceCapacity.put(userFriendlyKey, value);
            });

            model.addAttribute("deviceCapacity", transformedDeviceCapacity); // Map<String, Integer>
            deviceService.end();
        } else {
            model.addAttribute("deviceCapacity", null);
        }

        return "admin/deviceCapacity";
    }

    // Post methods
    @PostMapping("/synktimedate")
    public String setTime(Model model) throws IOException, DeviceNotConnectException {
        if (deviceService.connectTo()) {
            deviceService.synkTimDate();
            model.addAttribute("mgs", "Device Time Set Successfully.");
            model.addAttribute("localDateTime", LocalDateTime.now());
            deviceService.end();
            return "/admin/done";
        }
        return "/admin/deviceOffline";
    }

    @PostMapping("/add/user")
    public String setaddUser(EmployeeModel employeeModel) throws IOException {
        String mgs = deviceService.addUser(employeeModel);
        return "redirect:/admin/add/user?message=" + URLEncoder.encode(mgs, StandardCharsets.UTF_8);
    }

    @PostMapping("/add/attendance")
    public String addAttendance(AttendanceDTO attendanceDTO) throws IOException, ParseException {
        DateFormat outputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(attendanceDTO.getRecordTime());
        Date recordTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        AttendanceRecordModel attendanceRecordModel = new AttendanceRecordModel();

        attendanceRecordModel.setRecordTime(outputFormat.format(recordTime));
        attendanceRecordModel.setUserID(attendanceDTO.getUserID());
        attendanceRecordModel.setVerifyType(attendanceDTO.getVerifyType());
        attendanceRecordModel.setVerifyState(attendanceDTO.getVerifyState());


        String mgs = attendanceRecordService.insertData(attendanceRecordModel);
        return "redirect:/admin/add/attendance";
    }


    @PostMapping("/poweroff")
    public String DevicePowerDown(Model model) throws DeviceNotConnectException, IOException, ParseException {
        if (deviceService.connectTo()) {
            deviceService.devicePowerDown();
            model.addAttribute("mgs", "Device Shutdown Successfully.");
        }
        return "/admin/deviceOffline";
    }

    @PostMapping("/restart")
    public String DeviceRestart(Model model) throws DeviceNotConnectException, IOException, ParseException {
        if (deviceService.connectTo()) {
            deviceService.deviceRestart();
            model.addAttribute("mgs", "Device Shutdown Successfully.");
        }
        return "/admin/deviceOffline";
    }

    @PostMapping("/device/new")
    public String setaddDevice(DeviceModel deviceModel) throws IOException, ParseException {
        dBservice.addNewDevice(deviceModel);
        return "redirect:/admin/device/new";
    }

    @PostMapping("/list/user/enrollFinger")
    public String enrollFinger(@RequestParam("uID") int uID,
                              @RequestParam("finger_selection") int fingerSelection,
                              @RequestParam("empId") String empId) throws IOException {

        if (deviceService.connectTo()) {
            deviceService.enrollFinger(uID, fingerSelection,empId);
            deviceService.end();
        }

        return "redirect:/admin/list/user/device";
    }
}
