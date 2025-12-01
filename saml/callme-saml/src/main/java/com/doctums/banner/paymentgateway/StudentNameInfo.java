package com.doctums.banner.paymentgateway;

public class StudentNameInfo {
    private String firstName;
    private String lastName;
    // + add email, phone, etc. as needed

    public StudentNameInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
