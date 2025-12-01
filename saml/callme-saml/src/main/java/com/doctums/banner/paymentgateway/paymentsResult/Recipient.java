package com.doctums.banner.paymentgateway.paymentsResult;

import java.util.ArrayList;

public class Recipient {
    private String id;
    private ArrayList<Field> fields;
    private ArrayList<Item> items;

    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }
    public ArrayList<Field> getFields () {
        return fields;
    }
    public void setFields (ArrayList<Field> fields) {
        this.fields = fields;
    }
    public ArrayList<Item> getItems () {
        return items;
    }
    public void setItems (ArrayList<Item> items) {
        this.items = items;
    }
}
