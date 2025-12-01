package com.doctums.banner.paymentgateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;

@Component
@ConfigurationProperties(prefix = "langapp")
public class AppProperties implements InitializingBean {
    private String countryCode;
    private String language = "en"; // default

    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        // Debug message for Tomcat catalina.out
        System.out.println("[DEBUG] AppProperties - setCountryCode: " + countryCode);
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
        // Debug message for Tomcat catalina.out
        System.out.println("[DEBUG] AppProperties - setLanguage: " + language);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Notify on bean initialization so external Tomcat logs show the current values
        System.out.println("[DEBUG] AppProperties initialized. countryCode=" + countryCode + ", language=" + language);
    }
}
