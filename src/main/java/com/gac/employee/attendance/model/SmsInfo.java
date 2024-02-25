package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.SmsEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmsInfo {
    private SmsEnum tag;
    private int id;
    private int validMinutes;
    private int reserved;
    private long startTime;

    private String content;

}
