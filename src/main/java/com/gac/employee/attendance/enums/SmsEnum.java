package com.gac.employee.attendance.enums;

public enum SmsEnum {
    PUBLIC(253),
    PRIVATE(254),
    DRAFT(255);
    private final int smsvalue;
    SmsEnum(int smsvalue) {
        this.smsvalue = smsvalue;
    }

    public int getSmsvalue() {
        return smsvalue;
    }
}
