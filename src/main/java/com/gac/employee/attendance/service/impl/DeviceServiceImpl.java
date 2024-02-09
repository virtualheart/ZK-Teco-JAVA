package com.gac.employee.attendance.service.impl;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import com.gac.employee.attendance.gateway.DeviceGateway;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.repo.AttendanceRecordRepository;
import com.zkteco.commands.AttendanceRecord;
import com.zkteco.commands.UserInfo;
import com.zkteco.terminal.ZKTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gac.employee.attendance.service.DeviceService;
import com.zkteco.Exception.DeviceNotConnectException;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceGateway deviceGateway;
	@Autowired
	private AttendanceRecordRepository attendanceRecordRepo;

	ZKTerminal terminal;

//	public DeviceServiceImpl(DeviceGateway deviceGateway) throws IOException {
//		this.deviceGateway = deviceGateway;
//			try {
//				if (terminal==null || !terminal.testPing()){
//					this.terminal = deviceGateway.getClient();
//				}
//			} catch (DeviceNotConnectException e) {
//				System.out.println("Please check the device");
//			}
//	}

	public void connectto() throws DeviceNotConnectException, IOException {
		try {
			terminal = deviceGateway.getClient();

//			terminal= new ZKTerminal("192.168.1.201", 4370);
//			terminal.connect();
//			terminal.connectAuth(0);
		}catch (DeviceNotConnectException e){
			System.out.println();
		}
	}

	@Override
	public void addNewEmployee(UserInfo userInfo) throws IOException, ParseException, DeviceNotConnectException {
		if (terminal != null) {
			terminal.modifyUserInfo(userInfo);
		}
	}

	@Override
	public boolean synkTimDate() throws IOException, DeviceNotConnectException {
		terminal.syncTime();
//		terminal.disconnect();
		return true;
	}

	@Override
	public String GetDeviceTimeDate() throws IOException, ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a - dd/MM/yyyy");
		Date date;
		try{
			date = terminal.getDeviceTime();
			return simpleDateFormat.format(date);
		} catch (Exception e){
			System.out.println();

		}
//		System.out.println(date);
		return simpleDateFormat.format(new Date());
	}

	@Override
	public boolean deviceOnlineCheck()  {
		if (terminal != null) {
			boolean test = terminal.testPing();
//			terminal.disconnect();
			return test;
		}
		return false;
	}

	@Override
	public List<AttendanceRecordModel> getAttendanceList() throws ParseException, IOException, DeviceNotConnectException {

		if (terminal != null) {
			terminal.disableDevice();
			List<AttendanceRecord> allAttendance = terminal.getAttendanceRecords();
			terminal.enableDevice();
//			terminal.disconnect();

			List<AttendanceRecordModel> attendanceRecords = new ArrayList<>();
			for (AttendanceRecord record : allAttendance) {
				AttendanceRecordModel model = new AttendanceRecordModel();
				model.setUserSN(record.getUserSN());
				model.setUserID(String.valueOf(record.getUserID()));
				model.setVerifyType(AttendanceType.valueOf(record.getVerifyType().name()));
				model.setRecordTime(record.getRecordTime());
				model.setVerifyState(AttendanceState.valueOf(record.getVerifyState().name()));

				attendanceRecords.add(model);
			}
			Collections.reverse(attendanceRecords);

			return attendanceRecords;
		}
		return null;
	}

	public void end() throws IOException {
		if (terminal != null)
			terminal.disconnect();
	}
}