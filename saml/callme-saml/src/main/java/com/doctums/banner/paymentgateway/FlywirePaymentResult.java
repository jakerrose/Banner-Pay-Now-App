package com.doctums.banner.paymentgateway;

public class FlywirePaymentResult {

    private ConfirmUrl confirm_url;
    private Payor payor;
    private String source;
    private String reference;
    private boolean success;

    // Getters
    public ConfirmUrl getConfirm_url() {
        return confirm_url;
    }

    public Payor getPayor() {
        return payor;
    }

    public String getSource() {
        return source;
    }

    public String getReference() {
        return reference;
    }

    public boolean isSuccess() {
        return success;
    }

    // Inner classes for nested JSON fields
    public static class ConfirmUrl {
        private String method;
        private String url;

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class Payor {
        private String email;

        public String getEmail() {
            return email;
        }
    }
}
