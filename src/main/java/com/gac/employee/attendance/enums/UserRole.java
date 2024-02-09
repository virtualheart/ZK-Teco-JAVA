package com.gac.employee.attendance.enums;

public enum UserRole {
    USER_DEFAULT(0),
    USER_ENROLLER(2),
    USER_MANAGER(6),
    USER_ADMIN(14);

    private final int role;
    UserRole(int role){
        this.role = role;
    }

    public int UserRole() {
        return role;
    }
}
