package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.TaskFrequency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class ScheduleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cranStartTime;
    private String taskName;
    private TaskFrequency taskFrequncy;
    private String lastStartTime;
    private String LastRuntime;
    private String nextRunTime;
    private String cranEndTime;
}
