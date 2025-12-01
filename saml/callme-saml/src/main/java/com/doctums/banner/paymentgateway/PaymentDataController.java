package com.doctums.banner.paymentgateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import com.doctums.banner.paymentgateway.PaymentData;


@RestController
@RequestMapping("/api")
public class PaymentDataController {

    @PostMapping("/payment-data")
    public ResponseEntity<String> receivePaymentData(@RequestBody PaymentData paymentData) {
        System.out.println("Received payment data: " + paymentData);
        // You can store it, log it, validate it, etc.
        return ResponseEntity.ok("Data received");
    }
    @PostMapping("/payment-data-raw")
    public ResponseEntity<String> receiveRaw(@RequestBody String rawJson) {
        System.out.println("RAW JSON: " + rawJson);
        return ResponseEntity.ok("Logged raw JSON");
    }
}
