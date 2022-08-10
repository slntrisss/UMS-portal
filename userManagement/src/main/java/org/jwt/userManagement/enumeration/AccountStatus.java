package org.jwt.userManagement.enumeration;

public enum AccountStatus {
    LOCKED("LOCKED"),
    NON_LOCKED("NON_LOCKED");

    private final String accountStatus;

    AccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }
}
