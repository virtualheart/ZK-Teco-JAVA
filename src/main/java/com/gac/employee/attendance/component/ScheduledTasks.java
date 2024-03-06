/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gac.employee.attendance.component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gac.employee.attendance.repo.ScheduleRepository;
import com.zkteco.Exception.DeviceNotConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Autowired
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired
	private AttendanceDataProcess attendanceDataProcess;
	@Autowired
	private ScheduleRepository scheduleRepository;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Scheduled(cron = "0 0/1 * 1/1 * ?") // Runs every 15 minute
//	@Scheduled(cron = "0 0 * * * *") // Runs every hour at the 0th minute
	public void attendancePullHourlyCron() throws IOException, DeviceNotConnectException, ParseException {
		log.info("[attendancePullHourlyCron], The time is now {}", dateFormat.format(new Date()));
		if (attendanceDataProcess.pullAttendanceFromDevice()){
			log.info("Attendance Pulled successfully");
		} else {
			log.error("Attendance Pulled Failed...!");
		}
	}

	@Scheduled(cron = "0 0 15 * * 5") // Runs every Friday at 3 PM (15:00)
	public void ReportGenWeeklyCron() throws IOException {
		log.info("The time is now ReportGenWeeklyCron {}", dateFormat.format(new Date()));
	}

	@Scheduled(cron = "0 0 10 1 * ?") // Runs at 10 AM on the 1st day of the month
	@Scheduled(cron = "0 15 9-17 * * MON-SAT")
	public void ReportGenMonthlyCron() throws IOException {
		log.info("The time is now ReportGenMonthlyCron {}", dateFormat.format(new Date()));
	}

	@Scheduled(cron = "0 0 18 28-31 * ?")
	public void myLastDayOfMonthJob() {
		Calendar calendar = Calendar.getInstance();

		// Check if it's the last day of the month
		if (calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DATE)) {

			System.out.println("Running task on the last 4 days of the month at 6 PM.");
		}
	}

}