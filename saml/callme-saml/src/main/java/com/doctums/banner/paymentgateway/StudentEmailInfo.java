package com.doctums.banner.paymentgateway;

public class StudentEmailInfo {
    private String email;
    // + add email, phone, etc. as needed

    public StudentEmailInfo(String email) {

        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
