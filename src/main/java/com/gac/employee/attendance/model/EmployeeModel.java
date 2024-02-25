package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.UserRole;
import com.zkteco.commands.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="employee")
public class EmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private int employeeId;

    private String employeeName;
    @Enumerated(EnumType.ORDINAL)
    private UserRole userRole;

    @Column(nullable = true)
    private int password;
    @Column(nullable = true)
    private int cardNumber;

}
