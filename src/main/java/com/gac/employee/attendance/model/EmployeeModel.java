package com.gac.employee.attendance.model;

import com.gac.employee.attendance.enums.UserRole;
import com.zkteco.commands.UserInfo;
import jakarta.persistence.*;

@Entity
@Table(name="employee")
public class EmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(unique = true)
    private int employeeId;

    private String employeeName;
    @Enumerated(EnumType.ORDINAL)
    private UserRole userRole;

    private int password;
//    Column(unique=true)
    private int cardNumber;

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
}
