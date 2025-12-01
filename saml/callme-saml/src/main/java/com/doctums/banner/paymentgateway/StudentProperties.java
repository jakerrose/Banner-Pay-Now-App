package com.doctums.banner.paymentgateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "student")
public class StudentProperties {
    private String attributeSource;

    public String getAttributeSource() {
        return attributeSource;
    }

    public void setAttributeSource(String attributeSource) {
        this.attributeSource = attributeSource;
    }
}
