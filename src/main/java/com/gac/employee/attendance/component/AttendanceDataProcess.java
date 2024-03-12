package com.gac.employee.attendance.component;

import com.gac.employee.attendance.AttendanceApplication;
import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import com.gac.employee.attendance.enums.TaskFrequency;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.ScheduleModel;
import com.gac.employee.attendance.repo.AttendanceRecordRepository;
import com.gac.employee.attendance.repo.ScheduleRepository;
import com.gac.employee.attendance.service.DeviceService;
import com.zkteco.Exception.DeviceNotConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class AttendanceDataProcess {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    private static final Logger log = LoggerFactory.getLogger(AttendanceDataProcess.class);

    public boolean pullAttendanceFromDevice() throws IOException, DeviceNotConnectException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2024-02-21 00:00:00
        if (deviceService.connectTo()) {

            Optional<ScheduleModel> scheduleOptional = scheduleRepository.findTopByTaskName("AttendanceFromDevice");

            if (scheduleOptional.isPresent()) {
                ScheduleModel schedule = scheduleOptional.get();

                String startDate = schedule.getCranEndTime();
                Date endDate = new Date();

                try {
                    List<AttendanceRecordModel> attendanceRecords = deviceService.getAttendanceListFromRange(startDate, simpleDateFormat.format(endDate));
                    List<AttendanceRecordModel> attendanceData = new ArrayList<>();

                    for (AttendanceRecordModel record : attendanceRecords) {
                        AttendanceRecordModel model = convertToAttendanceRecordModel(record);
                        attendanceData.add(model);
                    }
                    attendanceRecordRepository.saveAll(attendanceData);

                    schedule.setCranEndTime(simpleDateFormat.format(endDate));
                    schedule.setLastRuntime(simpleDateFormat.format(new Date()));

                    scheduleRepository.save(schedule);
                    deviceService.end();
                    return true;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    String LastRuntime= simpleDateFormat.format(new Date());
                    schedule.setLastRuntime(LastRuntime);
                    scheduleRepository.save(schedule);
                    deviceService.end();
                    return false;
                }
            } else{
                String dateTimeString = "2000-01-01 00:00:10"; //yyyy-MM-dd HH:mm:ss
                String LastRuntime= simpleDateFormat.format(new Date());
                ScheduleModel schedule = new ScheduleModel();
                schedule.setTaskName("AttendanceFromDevice");
                schedule.setLastRuntime(LastRuntime);
                schedule.setLastStartTime(LastRuntime);
                schedule.setCranEndTime(dateTimeString);
                schedule.setTaskFrequncy(TaskFrequency.HOURLY);
//                schedule.setCranEndTime(new Date().toString());
                scheduleRepository.save(schedule);

            }
        }
        return false;
    }

    private AttendanceRecordModel convertToAttendanceRecordModel(AttendanceRecordModel attendanceRecord) {
        AttendanceRecordModel attendanceRecordModel = new AttendanceRecordModel();
        attendanceRecordModel.setUserID(attendanceRecord.getUserID());
        attendanceRecordModel.setUserSN(attendanceRecord.getUserSN());
        attendanceRecordModel.setRecordTime(attendanceRecord.getRecordTime());
        attendanceRecordModel.setVerifyType(AttendanceType.valueOf(attendanceRecord.getVerifyType().name()));
        attendanceRecordModel.setVerifyState(AttendanceState.valueOf(attendanceRecord.getVerifyState().name()));

        return attendanceRecordModel;
    }
}
