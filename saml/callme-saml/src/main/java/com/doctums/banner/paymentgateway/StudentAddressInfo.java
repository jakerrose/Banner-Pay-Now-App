package com.doctums.banner.paymentgateway;

public class StudentAddressInfo {
    private String address1;
    private String address2;
    private String city;
    private String zip;
    private String country;
    private String state;
    private String nationCode;

    public StudentAddressInfo(String address1, String address2, String city, String state, String zip, String nationCode) {


        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.nationCode = nationCode;
    }


    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getNationCode() {
        return nationCode;
    }
    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }
}
