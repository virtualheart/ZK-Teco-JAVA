package com.gac.employee.attendance.component;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import com.gac.employee.attendance.enums.TaskFrequency;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.ScheduleModel;
import com.gac.employee.attendance.repo.AttendanceRecordRepository;
import com.gac.employee.attendance.repo.ScheduleRepository;
import com.gac.employee.attendance.service.DeviceService;
import com.zkteco.Exception.DeviceNotConnectException;
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

    public boolean pullAttendanceFromDevice() throws IOException, DeviceNotConnectException, ParseException {

        if (deviceService.connectTo()) {

            Optional<ScheduleModel> scheduleOptional = scheduleRepository.findTopByTaskFrequencyAndTaskName(TaskFrequency.HOURLY, "AttendaceFromDevice");

            if (scheduleOptional.isPresent()) {
                ScheduleModel schedule = scheduleOptional.get();

                Date startDate = schedule.getCranEndTime();
                Date endDate = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a - dd/MM/yyyy");

                try {
                    List<AttendanceRecordModel> attendanceRecords = deviceService.getAttendanceListFromRange(simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                    List<AttendanceRecordModel> attendanceData = new ArrayList<>();

                    for (AttendanceRecordModel record : attendanceRecords) {
                        AttendanceRecordModel model = convertToAttendanceRecordModel(record);
                        attendanceData.add(model);
                    }
                    attendanceRecordRepository.saveAll(attendanceData);

                    schedule.setCranEndTime(endDate);
                    schedule.setLastRuntime(new Date());

                    scheduleRepository.save(schedule);

                    return true;
                } catch (Exception e) {
                    // Update only LastRuntime on failure
                    schedule.setLastRuntime(new Date());
                    scheduleRepository.save(schedule);
                    return false;
                }
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
