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
    @Temporal(TemporalType.DATE)
    private Date cranStartTime;
    private String taskName;
    private TaskFrequency taskFrequncy;
    @Temporal(TemporalType.DATE)
    private Date lastStartTime;
    @Temporal(TemporalType.DATE)
    private Date LastRuntime;
    @Temporal(TemporalType.DATE)
    private Date nextRunTime;
    @Temporal(TemporalType.DATE)
    private Date cranEndTime;
}
