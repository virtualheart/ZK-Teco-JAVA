package com.gac.employee.attendance.service.impl;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.gateway.DeviceGateway;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.repo.AttendanceRecordRepository;
import com.gac.employee.attendance.service.AttendanceRecordService;
import com.zkteco.Exception.DeviceNotConnectException;
import com.zkteco.commands.AttendanceRecord;
import com.zkteco.terminal.ZKTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    @Autowired
    AttendanceRecordRepository attendanceRecordRepo;

    @Override
    public List<AttendanceRecordModel> getAttendanceFormDB() {
        return attendanceRecordRepo.findAll();
    }

    @Override
    public long getTodayCheckinCount() {
        long checkin = 0;
        String todayStart = String.valueOf(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        String todayEnd = String.valueOf(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1).toInstant()));
//        System.out.println(todayStart + " \n " + todayEnd);
        checkin = attendanceRecordRepo.findByRecordTimeAfterAndRecordTimeBeforeAndVerifyState(todayStart, todayEnd,AttendanceState.CHECK_IN).stream().count();
        return checkin;
    }

    @Override
    public long getTodayCheckoutCount() {
        long checkout = 0;
        String todayStart = String.valueOf(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        String todayEnd = String.valueOf(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1).toInstant()));
//        System.out.println(todayStart + " \n " + todayEnd);
        checkout = attendanceRecordRepo.findByRecordTimeAfterAndRecordTimeBeforeAndVerifyState(todayStart, todayEnd,AttendanceState.CHECK_OUT).stream().count();
        return checkout;
    }

    @Override
    public List<AttendanceRecordModel> getAttendanceRecordsLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String lastMonth = String.valueOf(calendar.getTime());

        return attendanceRecordRepo.findByRecordTimeGreaterThanEqual(lastMonth);
    }
}
