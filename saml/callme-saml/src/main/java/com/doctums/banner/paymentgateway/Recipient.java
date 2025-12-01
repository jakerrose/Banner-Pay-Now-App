package com.doctums.banner.paymentgateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Recipient {

    @JsonProperty("fields")
    private List<Field> fields;

    public Recipient() {}

    public Recipient(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
