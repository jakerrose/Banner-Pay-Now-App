package com.doctums.banner.paymentgateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "banner")
public class BannerProperties {
    private String academicSystem; // "semesters" or "quarters"

    public String getAcademicSystem() {
        return academicSystem;
    }

    public void setAcademicSystem(String academicSystem) {
        this.academicSystem = academicSystem;
    }
    private String addressType;

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    private Map<String, String> countryMapping;

    private String emailType;
    public String getEmailType() {
        return emailType;
    }
    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}
