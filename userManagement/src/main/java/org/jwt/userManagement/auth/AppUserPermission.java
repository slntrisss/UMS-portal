package org.jwt.userManagement.auth;

public enum AppUserPermission {
    USER_READ("USER_READ"),
    USER_UPDATE("USER_UPDATE"),
    USER_CREATE("USER_CREATE"),
    USER_DELETE("USER_DELETE");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
