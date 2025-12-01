package com.doctums.banner.paymentgateway.paymentsResult;

public class StatusTransitions {

    private Object guaranteed_at;
    private Object delivered_at;
    private Object cancelled_at;
    private Object authorized_at;

    public Object getGuaranteed_at() {
        return guaranteed_at;
    }
    public void setGuaranteed_at(Object guaranteed_at) {
        this.guaranteed_at = guaranteed_at;
    }
    public Object getDelivered_at() {
        return delivered_at;
    }
    public void setDelivered_at(Object delivered_at) {
        this.delivered_at = delivered_at;
    }
    public Object getCancelled_at() {
        return cancelled_at;
    }
    public void setCancelled_at(Object cancelled_at) {
        this.cancelled_at = cancelled_at;
    }
    public Object getAuthorized_at() {
        return authorized_at;
    }
}

