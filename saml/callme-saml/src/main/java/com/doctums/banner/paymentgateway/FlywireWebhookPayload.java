package com.doctums.banner.paymentgateway;
import java.util.Date;

public class FlywireWebhookPayload {

    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
    public class Data{
        public String payment_id;
        public String amount_from;
        public String currency_from;
        public String amount_to;
        public String currency_to;
        public String status;
        public Date expiration_date;
        public String external_reference;
        public String country;
        public PaymentMethod payment_method;
        public String recurring_id;
        public Fields fields;
        public Payer payer;
    }

    public class Fields{
        public String student_id;
        public String student_last_name;
    }

    public class Payer{
        public String first_name;
        public String last_name;
        public Object middle_name;
        public String address1;
        public Object address2;
        public String city;
        public String country;
        public Object state;
        public String zip;
        public String phone;
        public String email;
    }

    public class PaymentMethod{
        public String type;
    }

    public class Root{
        public String event_type;
        public Date event_date;
        public String event_resource;
        public Data data;
    }


}
