package com.gac.employee.attendance.component;

import org.springframework.stereotype.Component;

@Component
public class GenReports {
    public boolean weaklyReport(){
        System.out.println("Test weaklyReport");
        return true;
    }

    public boolean monthlyReport(){
        System.out.println("Test monthlyReport");
        return true;
    }

}
