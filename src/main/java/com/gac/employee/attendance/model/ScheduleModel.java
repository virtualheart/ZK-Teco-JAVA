package com.gac.employee.attendance.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Table(name = "schedule")
public class ScheduleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.DATE)
    private Date cranStartTime;
    @Temporal(TemporalType.DATE)
    private Date attPullStartTime;
    @Temporal(TemporalType.DATE)
    private Date attPullEndTime;
    @Temporal(TemporalType.DATE)
    private Date cranEndTime;

    public ScheduleModel(Integer id, Date cranStartTime, Date attPullStartTime, Date attPullEndTime, Date cranEndTime) {
        this.id = id;
        this.cranStartTime = cranStartTime;
        this.attPullStartTime = attPullStartTime;
        this.attPullEndTime = attPullEndTime;
        this.cranEndTime = cranEndTime;
    }

    public ScheduleModel() {

    }

}
