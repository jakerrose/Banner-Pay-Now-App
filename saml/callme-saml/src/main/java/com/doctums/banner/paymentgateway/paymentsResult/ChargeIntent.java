package com.doctums.banner.paymentgateway.paymentsResult;

public class ChargeIntent {
    private String initiator;
    private String mode;
    private String mandate_id;
    private String payor_id;
    private String payment_method_token;

    public String getInitiator() {
        return initiator;
    }
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getMandate_id() {
        return mandate_id;
    }
    public void setMandate_id(String mandate_id) {
        this.mandate_id = mandate_id;
    }
    public String getPayor_id() {
        return payor_id;
    }
    public void setPayor_id(String payor_id) {
        this.payor_id = payor_id;
    }
    public String getPayment_method_token() {
        return payment_method_token;
    }
    public void setPayment_method_token(String payment_method_token) {
        this.payment_method_token = payment_method_token;
    }

}
