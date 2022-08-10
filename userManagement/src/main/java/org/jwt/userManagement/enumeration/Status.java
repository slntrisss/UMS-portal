package org.jwt.userManagement.enumeration;

public enum Status {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
