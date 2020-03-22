package com.test.vo;

public class AccountModel {

    private String accountName;

    private int accountAge;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(int accountAge) {
        this.accountAge = accountAge;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "accountName='" + accountName + '\'' +
                ", accountAge=" + accountAge +
                '}';
    }
}
