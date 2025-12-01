package com.doctums.banner.paymentgateway;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Field {

    @JsonProperty("id")
    private String id;

    @JsonProperty("value")
    private String value;

    // Constructors
    public Field() {}

    public Field(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
