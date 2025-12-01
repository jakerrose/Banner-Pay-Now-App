package com.doctums.banner.paymentgateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.doctums.banner.paymentgateway.Field;

public class FlywireSessionRequest {
    private String type;
    private ChargeIntent charge_intent;
    private Payor payor;
    private Options options;
    private Recipient recipient;
    private List<Item> items;
    private String notifications_url;
    private String external_reference;
    @JsonProperty("recipient_id")
    private String recipient_id;
    @JsonProperty("payor_id")
    private String payor_id;
    private boolean enable_email_notifications;
//    private String success_url;

    public static class ChargeIntent {
        private String mode;
        public ChargeIntent() {}
        public ChargeIntent(String mode) { this.mode = mode; }
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
    }

    public static class Payor {
        private String first_name, last_name, address, city, country, state, phone, email, zip;

        public String getfirst_name() { return first_name; }

        public void setfirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getlast_name() {
            return last_name;
        }
        public void setlast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getaddress() {
            return address;
        }
        public void setaddress(String address) {
            this.address = address;
        }

        public String getcity() {
            return city;
        }
        public void setcity(String city){
            this.city = city;
        }

        public String getcountry() {
            return country;
        }
        public void setcountry(String country) {
            this.country = country;
        }

        public String getstate() {
            return state;
        }
        public void setstate(String state) {
            this.state = state;
        }

        public String getphone() {
            return phone;
        }
        public void setphone(String phone) {
            this.phone = phone;
        }

        public String getemail() {
            return email;
        }
        public void setemail(String email) {
            this.email = email;
        }

        public String getzip() {
            return zip;
        }
        public void setzip(String zip) {
            this.zip = zip;
        }
    }

    public static class Options {
        private Form form;
        public Form getForm() { return form; }
        public void setForm(Form form) { this.form = form; }
    }

    public static class Form {
        private String locale;
        private String action_button;
        public String getLocale() { return locale; }
        public void setLocale(String locale) { this.locale = locale; }
        public String getaction_button() { return action_button; }
        public void setaction_button (String action_button) {
            this.action_button = action_button;
        }
    }

    public static class Item {
        private String id;
        private int amount;
        public Item() {}
        public Item(String id, int amount) {
            this.id = id;
            this.amount = amount;
        }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
    }

    // Getters and setters for FlywireSessionRequest main fields
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public ChargeIntent getChargeIntent() { return charge_intent; }
    public void setChargeIntent(ChargeIntent charge_intent) { this.charge_intent = charge_intent; }
    public Payor getPayor() { return payor; }
    public void setPayor(Payor payor) { this.payor = payor; }
    public Options getOptions() { return options; }
    public void setOptions(Options options) { this.options = options; }
    public Recipient getRecipient() { return recipient; }
    public void setRecipient(Recipient recipient) { this.recipient = recipient; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public String getnotifications_url() { return notifications_url; }
    public void setnotifications_url(String notifications_url) { this.notifications_url = notifications_url; }
    public String getexternal_reference() { return external_reference; }
    public void setexternal_reference(String external_reference) { this.external_reference = external_reference; }
    public String getrecipient_id() { return recipient_id; }
    public void setrecipient_id(String recipient_id) { this.recipient_id = recipient_id; }
    public String getpayor_id() { return payor_id; }
    public void setpayor_id(String payor_id) { this.payor_id = payor_id; }
//    public String getsuccess_url() { return success_url; }
//    public void setsuccess_url(String sucess_url) { this.success_url = sucess_url; }
}
