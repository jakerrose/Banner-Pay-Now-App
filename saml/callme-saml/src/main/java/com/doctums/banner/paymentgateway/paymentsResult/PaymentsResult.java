package com.doctums.banner.paymentgateway.paymentsResult;

import java.util.ArrayList;
import java.util.Date;

public class PaymentsResult {
    private String payment_id;
    private Date created_at;
    private Date expiration_date;
    private String status;
    private String status_detail;
    private StatusTransitions status_transitions;
    private int amount_from;
    private String currency_from;
    private int amount_to;
    private String currency_to;
    private Payer payer;
    private Recipient recipient;
    private ArrayList<Item> items;
    private ChargeIntent charge_intent;
    public Metadata metadata;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    public StatusTransitions getStatus_transitions() {
        return status_transitions;
    }

    public void setStatus_transitions(StatusTransitions status_transitions) {
        this.status_transitions = status_transitions;
    }

    public int getAmount_from() {
        return amount_from;
    }

    public void setAmount_from(int amount_from) {
        this.amount_from = amount_from;
    }

    public String getCurrency_from() {
        return currency_from;
    }

    public void setCurrency_from(String currency_from) {
        this.currency_from = currency_from;
    }

    public int getAmount_to() {
        return amount_to;
    }

    public void setAmount_to(int amount_to) {
        this.amount_to = amount_to;
    }

    public String getCurrency_to() {
        return currency_to;
    }

    public void setCurrency_to(String currency_to) {
        this.currency_to = currency_to;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ChargeIntent getCharge_intent() {
        return charge_intent;
    }

    public void setCharge_intent(ChargeIntent charge_intent) {
        this.charge_intent = charge_intent;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
