package com.zkteco.commands;

public enum UserRole {
	USER_DEFAULT(0),
	USER_ENROLLER(2),
	USER_MANAGER(6),
	USER_ADMIN(14);

    private final int role;

    private UserRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

}
