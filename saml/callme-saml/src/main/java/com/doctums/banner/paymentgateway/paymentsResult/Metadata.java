package com.doctums.banner.paymentgateway.paymentsResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
    private String payor_id;
    private String tracking_url;
    @JsonProperty("MyOwnMetadata")
    private String myOwnMetadata;

    public String getPayor_id() {
        return payor_id;
    }
    public void setPayor_id(String payor_id) {
        this.payor_id = payor_id;
    }
    public String getTracking_url() {
        return tracking_url;
    }
    public void setTracking_url(String tracking_url) {
        this.tracking_url = tracking_url;
    }
    public String getMyOwnMetadata() {
        return myOwnMetadata;
    }
    public void setMyOwnMetadata(String myOwnMetadata) {
        this.myOwnMetadata = myOwnMetadata;
    }
}
