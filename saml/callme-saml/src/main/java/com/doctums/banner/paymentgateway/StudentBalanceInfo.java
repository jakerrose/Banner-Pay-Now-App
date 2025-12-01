package com.doctums.banner.paymentgateway;

import java.math.BigDecimal;

public class StudentBalanceInfo {
    private BigDecimal accountBalance;
    private String termCode;

    public StudentBalanceInfo(BigDecimal accountBalance, String termCode) {
        this.accountBalance = accountBalance;
        this.termCode = termCode;
    }
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
    public String getTermCode() {
        return termCode;
    }
    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }
}
