package com.gac.employee.attendance.model;

import jakarta.persistence.*;
import org.hibernate.type.NumericBooleanConverter;

@Entity
@Table(name = "app_user")
public class AppUserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appUserid;
    private String userName;
    private String password;
    @Convert(converter = NumericBooleanConverter.class)
    private boolean userStatus;
    private String roles;
    public int getAppUserid() {
        return appUserid;
    }

    public void setAppUserid(int appUserid) {
        this.appUserid = appUserid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
