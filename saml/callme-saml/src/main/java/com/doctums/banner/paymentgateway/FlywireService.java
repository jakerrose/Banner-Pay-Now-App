package com.doctums.banner.paymentgateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FlywireService {

    private static final Logger logger = LoggerFactory.getLogger(FlywireService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final FlywireProperties flywireProperties;

    @Value("${api.secret}")
    private String apiSecret;

    public FlywireService(FlywireProperties flywireProperties) {
        this.flywireProperties = flywireProperties;
    }

    public FlywireSessionResponse generateSession(String firstName, String lastName, String email, String student_id, String accountBalance,
                                                  String address, String city, String state, String zip, String country, String phone, int inputAmt, String termCode, String inputEmail) {
        //String url = "https://api-platform-sandbox.flywire.com/payments/v1/checkout/sessions";
        String url = flywireProperties.getCheckoutUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Authentication-Key", apiSecret);

        try {
            FlywireSessionRequest requestBodyObject = buildRequestBody(firstName, lastName, email, student_id, accountBalance, address, city, state, zip, country, phone, inputAmt, termCode, inputEmail);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(requestBodyObject);

            logger.info("Flywire request body: {}", requestBody);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            logger.info("Flywire response: {}", response.getBody());
            logger.info("Serialized JSON:\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyObject));


            return mapper.readValue(response.getBody(), FlywireSessionResponse.class);

        } catch (Exception e) {
            logger.error("Error creating Flywire session", e);
            throw new RuntimeException("Failed to create Flywire session", e);
        }
    }

    private FlywireSessionRequest buildRequestBody(String firstName, String lastName, String email, String student_id, String accountBalance,
                                                   String address, String city, String state, String zip, String country, String phone, Integer inputAmt, String termCode, String inputEmail) {
        FlywireSessionRequest request = new FlywireSessionRequest();

        request.setType("one_off");
        request.setChargeIntent(new FlywireSessionRequest.ChargeIntent("one_off"));

        FlywireSessionRequest.Payor payor = new FlywireSessionRequest.Payor();
        payor.setfirst_name(firstName);
        payor.setlast_name(lastName);
        payor.setaddress(address);
        payor.setcity(city);
        payor.setcountry(country);
        payor.setstate(state);
        payor.setphone(phone);
        payor.setemail(email);
        payor.setzip(zip);
       request.setPayor(payor);

        FlywireSessionRequest.Form form = new FlywireSessionRequest.Form();
        form.setLocale("en");
        form.setaction_button("save");

        FlywireSessionRequest.Options options = new FlywireSessionRequest.Options();
        options.setForm(form);
        request.setOptions(options);

        List<Field> fields = List.of(
                new Field("client_code", "client_code"),
                new Field("detail_code", "detail_code"),
                new Field("termCode", termCode),
                new Field("student_id", student_id),
                new Field("student_first_name", firstName),
                new Field("student_last_name", lastName),
                new Field("student_email", email)
        );

        Recipient recipient = new Recipient(fields);
        request.setRecipient(recipient);

        FlywireSessionRequest.Item item = new FlywireSessionRequest.Item("default", inputAmt);
        request.setItems(List.of(item));
        //url from cloudflare tunnel
        request.setnotifications_url("https://story-risks-carefully-compromise.trycloudflare.com/api/flywire/webhook");
        request.setexternal_reference("My payment reference");
        request.setrecipient_id("DOX");
        request.setpayor_id("my_payor_id");

        return request;
    }
    public String confirmSession(String confirmUrl) {
        logger.info("Invoking confirmSession with URL: {}", confirmUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Authentication-Key", apiSecret);

        HttpEntity<String> entity = new HttpEntity<>("", headers); // empty body per Flywire API

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    confirmUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            logger.info("Flywire confirmation POST status: {}", response.getStatusCode());
            logger.debug("Flywire raw confirmation response: {}", response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());

            String paymentReference = json.path("payment_reference").asText();
            int amount = json.path("charge_info").path("amount").asInt();
            String currency = json.path("charge_info").path("currency").asText();
            String payorEmail = json.path("payor").path("email").asText();
            String payorCountry = json.path("payor").path("country").asText();
            String payorState = json.path("payor").path("state").asText();

            logger.debug("Payment Confirmation Details:");
            logger.debug("Flywire Payment Reference: {}", paymentReference);
            logger.debug("Charge Amount: {} {}", amount, currency);
            logger.debug("Payor Email: {}", payorEmail);
            logger.debug("Payor Country: {}", payorCountry);
            logger.debug("Payor State: {}", payorState);

            return paymentReference;

        } catch (HttpStatusCodeException e) {
            logger.error("Flywire confirmation failed with status {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error confirming Flywire session", e);
            return null;
        }
    }
    public String getTrackingId(String referenceId) {
        //String payUrl = "https://api-platform-sandbox.flywire.com/payments/v1/payments/" + referenceId;
        String payUrl = flywireProperties.getPaymentUrl() + referenceId;
        {
            logger.info("Invoking getTrackingId with URL: {}", payUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Authentication-Key", apiSecret);

            HttpEntity<String> entity = new HttpEntity<>("", headers); // empty body per Flywire API

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        payUrl,
                        HttpMethod.GET,
                        entity,
                        String.class
                );
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(response.getBody());

                String trackingUrl = json.path("metadata").path("tracking_url").asText();

                return trackingUrl;

            } catch (HttpStatusCodeException e) {
                logger.error("Flywire confirmation failed with status {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return null;
            } catch (Exception e) {
                logger.error("Unexpected error confirming Flywire session", e);
                return null;
            }
        }
    }
}
