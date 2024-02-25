package com.gac.employee.attendance.service.impl;

import java.io.IOException;

import java.net.BindException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gac.employee.attendance.enums.AttendanceState;
import com.gac.employee.attendance.enums.AttendanceType;
import com.gac.employee.attendance.enums.SmsEnum;
import com.gac.employee.attendance.gateway.DeviceGateway;
import com.gac.employee.attendance.model.AttendanceRecordModel;
import com.gac.employee.attendance.model.SmsInfo;
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

	public boolean connectTo() throws IOException {
		try {
			terminal = deviceGateway.getClient();

//			terminal= new ZKTerminal("192.168.1.201", 4370);
//			terminal.connect();
//			terminal.connectAuth(0);

			return true;
		}catch (DeviceNotConnectException e){
			System.out.println();
		} catch (BindException e){
			System.out.println("BindException: " + e.getMessage());
			if (e.getMessage().contains("Address already in use")) {
				System.out.println("Address already in use. Closing the connection.");
				terminal.disconnect();
			} else {
				System.out.println("Other BindException: " + e.getMessage());
			}
		}
		return false;
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
	public void devicePowerDown() throws IOException, ParseException {
		terminal.Poweroff();
		terminal = null;
	}

	@Override
	public void deviceRestart() throws IOException, ParseException {
		terminal.restart();
		terminal = null;
	}

	@Override
	public String getDeviceTimeDate() throws IOException, ParseException {
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

	@Override
	public String getDeviceName() throws IOException {
		if (terminal != null) {
            return terminal.getDeviceName();
		}
		return null;
	}

	@Override
	public Map<String, Integer> getDeviceCapacity() throws IOException {
		if (terminal != null) {
            return terminal.getDeviceStatus();
		}
		return null;
	}

	@Override
	public String getSerialNumber() throws IOException {
		if (terminal != null) {
			return terminal.getSerialNumber();
		}
		return null;
	}

	@Override
	public String getMAC() throws IOException {
		if (terminal != null) {
			return terminal.getMAC();
		}
		return null;
	}

	@Override
	public String getFaceVersion() throws IOException {
		if (terminal != null) {
			return terminal.getFaceVersion();
		}
		return null;
	}

	@Override
	public String getPlatform() throws IOException {
		if (terminal != null) {
			return terminal.getPlatform();
		}
		return null;
	}

	@Override
	public boolean getWorkCode() throws IOException {
		if (terminal != null) {
			return terminal.getWorkCode();
		}
		return false;
	}

	@Override
	public int getFPVersion() throws IOException {
		if (terminal != null) {
			return terminal.getFPVersion();
		}
		return 0;
	}

	@Override
	public String getOEMVendor() throws IOException {
		if (terminal != null) {
			return terminal.getOEMVendor();
		}
		return null;
	}

	@Override
	public List<SmsInfo> getSmsList() throws IOException, ParseException {
		List<SmsInfo> smsInfoList = new ArrayList<>();

		if (terminal != null) {
			int i = 1;
			com.zkteco.commands.SmsInfo smslist;

			do {
				smslist = terminal.getSms(i);

				if (smslist != null) {
					SmsInfo smsInfo = new SmsInfo();
					smsInfo.setId(smslist.getId());
					switch (smslist.getTag()) {
						case 253:
							smsInfo.setTag(SmsEnum.PUBLIC);
							break;
						case 254:
							smsInfo.setTag(SmsEnum.PRIVATE);
							break;
						case 255:
							smsInfo.setTag(SmsEnum.DRAFT);
							break;
					}
					smsInfo.setContent(smslist.getContent());
					smsInfo.setStartTime(smslist.getStartTime());
					smsInfo.setValidMinutes(smslist.getValidMinutes());
					smsInfo.setReserved(smslist.getReserved());

					smsInfoList.add(smsInfo);
				}

				i++;
			} while (smslist != null);

			return smsInfoList;
		}

		return null;
	}

	@Override

	public void end() throws IOException {
		if (terminal != null)
			terminal.disconnect();
	}
}