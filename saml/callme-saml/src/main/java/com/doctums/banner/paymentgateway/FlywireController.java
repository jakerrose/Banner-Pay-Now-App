package com.doctums.banner.paymentgateway;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import com.doctums.banner.paymentgateway.paymentsResult.PaymentsResult;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;



@RestController
@RequestMapping("/api/flywire")
public class FlywireController {

    private static final Logger logger = LoggerFactory.getLogger(FlywireController.class);

    private final FlywireService flywireService;
    private final StudentProperties studentProperties;
    private final StudentService studentService;
    private final TermCodeService termCodeService;

    @Autowired
    private AppProperties appProperties;


    public FlywireController(FlywireService flywireService,StudentProperties studentProperties, StudentService studentService, TermCodeService termCodeService) {
        this.flywireService = flywireService;
        this.studentProperties = studentProperties;
        this.studentService = studentService;
        this.termCodeService = termCodeService;
    }
        @PostMapping("/session")
        public ResponseEntity<FlywireSessionResponse> postFlywireSession(
                @AuthenticationPrincipal Saml2AuthenticatedPrincipal principal,
                @RequestParam(name = "inputAmt") Integer inputAmt,
                @RequestParam(name = "inputEmail") String inputEmail){

            logger.info("POST request for Flywire session with inputAmt={}", inputAmt);

            if (inputAmt == null) {
                logger.warn("inputAmt is missing from POST request");
                return ResponseEntity.badRequest().build();
            }

    try {
        String source = studentProperties.getAttributeSource();
        String studentId = principal.getFirstAttribute("student_id");
        // Query Banner for student info using StudentService
       String firstName = null;
        String lastName = null;
        String email = null;
        String accountBalance = null;
        String address = null;
        String city = null;
        String state = null;
        String zip = null;
        String country = null;
        String phone = null;
        String termCode = null;
        String nationCode = null;

            StudentNameInfo studentNameInfo = studentService.getStudentInfoById(studentId);
            if (studentNameInfo != null) {
                firstName = studentNameInfo.getFirstName();
                lastName = studentNameInfo.getLastName();
            }
//        StudentEmailInfo studentEmailInfo = studentService.getEmailById(studentId);
//        if (studentEmailInfo != null) {
//            email = studentEmailInfo.getEmail();
//        }
        email = inputEmail;
        StudentBalanceInfo studentBalanceInfo = studentService.getBalanceById(studentId);
        if(studentBalanceInfo != null) {
            BigDecimal balance = studentBalanceInfo.getAccountBalance();
            if(balance == null) {
                balance = BigDecimal.ZERO;
            }
            logger.info("Account balance in decimal: {}", balance);
            // Build locale from config
            Locale locale = new Locale(appProperties.getLanguage(), appProperties.getCountryCode());
            // Format currency
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            accountBalance = currencyFormatter.format(balance);
            logger.info("Formatted account balance: {}", accountBalance);
        }
        else
        {
            accountBalance = "0.00";
        }

        StudentAddressInfo studentAddressInfo = studentService.getAddressInfoById(studentId);
        if (studentAddressInfo != null) {
            String address1 = studentAddressInfo.getAddress1();
            String address2 = studentAddressInfo.getAddress2();

            if (address2 != null && !address2.isBlank()) {
                address = address1 + " " + address2;
            } else {
                address = address1;
            }
            city = studentAddressInfo.getCity();
            state = studentAddressInfo.getState();
            zip = studentAddressInfo.getZip();
            nationCode = studentAddressInfo.getNationCode();
        } else {
            logger.warn("No address info found for studentId: {}", studentId);
        }
        StudentNationInfo studentNationInfo = studentService.getNationName(nationCode);
        if(studentNationInfo != null) {
            country = studentNationInfo.getNationName();
        }
        if( country == null && nationCode == null) {
            country = "US"; // default to US if null
        }

            StudentPhoneInfo studentPhoneInfo = studentService.getPhoneById(studentId);
            if (studentPhoneInfo != null) {
                String areaCode = studentPhoneInfo.getAreaCode();
                String phoneNumber = studentPhoneInfo.getPhoneNumber();
                phone = 1 + areaCode + phoneNumber;
            }
            //Choose quarters or semesters in application.yml
            if(studentBalanceInfo != null)
            {
                termCode = studentBalanceInfo.getTermCode();
                termCode = termCodeService.translateTermCode(termCode);
            }

                FlywireSessionResponse response = flywireService.generateSession(
                        firstName, lastName, email, studentId, accountBalance, address, city, state, zip, country, phone, inputAmt, termCode, inputEmail);

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("Error generating Flywire session (POST)", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

    @PostMapping("/payment-result")
    public ResponseEntity<Map<String, String>> handleFlywireResult(@RequestBody FlywirePaymentResult result,
                                                                   HttpSession session) {
        String confirmUrl = result.getConfirm_url().getUrl();

        // Call Flywire confirmSession, which returns paymentReference
        String paymentReference = flywireService.confirmSession(confirmUrl);

        // Store in session for the /success page
        session.setAttribute("reference", paymentReference);
        Map<String, String> resp = new HashMap<>();
        resp.put("paymentReference", paymentReference);
        return ResponseEntity.ok(resp);
    }
    @PostMapping("/payments")
    public ResponseEntity<Map<String, String>> handleFlywirePayments(@RequestBody PaymentsResult response,
                                                                     HttpSession session) {
        logger.info("PaymentsResponse.id: {}", response.getPayment_id());
        String referenceId = response.getPayment_id();
        logger.info("Received Flywire payment reference: {}", referenceId);
        //call getTrackingId method to get tracking URL
        String trackingUrl = flywireService.getTrackingId(referenceId);

        // Store in session for the /success page
        session.setAttribute("tracking_url", trackingUrl);
        logger.info("trackingUrl: {}", trackingUrl);
        //return ResponseEntity.ok().build();
        Map<String, String> resp = new HashMap<>();
        resp.put("tracking_url", trackingUrl);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        try {
            System.out.println(">>> Inside /success controller");
            model.addAttribute("reference", session.getAttribute("reference"));
            String trackingUrl = (String) session.getAttribute("tracking_url");
            model.addAttribute("tracking_url", trackingUrl);
        }
        catch (Exception e) {
            logger.error("Exception in /success", e);
        }
        return "success";
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody FlywireWebhookPayload payload,
                                                @RequestHeader Map<String, String> headers) {
        logger.info("Received Flywire webhook:");
        logger.info("Headers: {}", headers);
        logger.info("Payload: {}", payload);

        return ResponseEntity.ok("Webhook received");
    }
}


