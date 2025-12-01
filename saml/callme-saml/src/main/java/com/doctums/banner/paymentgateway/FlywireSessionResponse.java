package com.doctums.banner.paymentgateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

public class FlywireSessionResponse {
    private String id;
    private String expires_at;
    private int expires_in_seconds;
    private HostedForm hosted_form;
    private List<Warning> warnings;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getExpires_at() { return expires_at; }
    public void setExpires_at(String expires_at) { this.expires_at = expires_at; }

    public int getExpires_in_seconds() { return expires_in_seconds; }
    public void setExpires_in_seconds(int expires_in_seconds) { this.expires_in_seconds = expires_in_seconds; }

    public HostedForm getHosted_form() { return hosted_form; }
    public void setHosted_form(HostedForm hosted_form) { this.hosted_form = hosted_form; }

    public List<Warning> getWarnings() {
        return warnings;
    }
    public void setWarnings(List<Warning> warnings) {
            this.warnings = warnings;
    }

    public static class HostedForm {
        private String url;
        private String method;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Warning {
        private String code;
        private String message;

        // Getters and setters (or use Lombok @Data)
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
