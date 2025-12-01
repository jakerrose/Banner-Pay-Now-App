package com.doctums.banner.paymentgateway;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

import java.math.BigDecimal;
import java.util.Locale;
import java.text.NumberFormat;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {


    private FlywireService flywireService;
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final StudentProperties studentProperties;
    private final StudentService studentService;
    @Value("${api.public-key}")
    private String frontendKey;
    @Autowired
    private AppProperties appProperties;
    private final FlywireProperties flywireProperties;

    public HomeController(FlywireService flywireService, StudentProperties studentProperties,
      StudentService studentService, FlywireProperties flywireProperties
    ) {
        this.studentProperties = studentProperties;
        this.flywireService = flywireService;
        this.studentService = studentService;
        this.flywireProperties = flywireProperties;
    }
@GetMapping({"/","/index"})
public String index(Authentication authentication, Model model, @RequestParam(name = "inputAmt", required = false) Integer inputAmt, RedirectAttributes redirectAttributes) {

    if (authentication != null && authentication.getPrincipal() instanceof Saml2AuthenticatedPrincipal principal) {

        // DEBUG: Print all SAML attributes
        principal.getAttributes().forEach((key, value) -> {
            logger.info("SAML Attribute -> {} = {}", key, value);
        });

        String source = studentProperties.getAttributeSource();
        logger.info("Attribute source: {}", source);

        String studentId = principal.getFirstAttribute("student_id");
        String firstName = null;
        String lastName = null;
        String email = null;
        String accountBalance = "0.00";
        BigDecimal rawAccountBalance = null;
        String address = null;
        String city = null;
        String state = null;
        String zip = null;
        String country = null;
        String phone = null;
        String termCode = null;
        String nationCode = null;
        String sdkUrl = null;

            StudentNameInfo studentNameInfo = studentService.getStudentInfoById(studentId);
            if (studentNameInfo != null) {
                firstName = studentNameInfo.getFirstName();
                lastName = studentNameInfo.getLastName();
            }

            StudentEmailInfo studentEmailInfo = studentService.getEmailById(studentId);
            if (studentEmailInfo != null) {
                email = studentEmailInfo.getEmail();
            }

            StudentBalanceInfo studentBalanceInfo = studentService.getBalanceById(studentId);
            if( studentBalanceInfo != null) {
                rawAccountBalance = studentBalanceInfo.getAccountBalance();
            }
            logger.info("Raw account balance: {}", rawAccountBalance);

            if( studentBalanceInfo != null) {
                accountBalance = String.valueOf(studentBalanceInfo.getAccountBalance());
            }
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
            if(studentBalanceInfo != null) {
                termCode = studentBalanceInfo.getTermCode();
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
            }
            StudentNationInfo studentNationInfo = studentService.getNationName(nationCode);
            if(studentNationInfo != null) {
                country = studentNationInfo.getNationName();
                logger.info("short country name: {}", country);
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
            sdkUrl = flywireProperties.getSdkUrl();


        model.addAttribute("frontendKey", frontendKey);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("student_id", studentId);
        model.addAttribute("email", email);
        model.addAttribute("account_balance", accountBalance);
        model.addAttribute("raw_account_balance", rawAccountBalance);
        model.addAttribute("address", address);
        model.addAttribute("city", city);
        model.addAttribute("state", state);
        model.addAttribute("zip", zip);
        model.addAttribute("country", country);
        model.addAttribute("phone", phone);
        model.addAttribute("term_code", termCode);
        model.addAttribute("sdkUrl", sdkUrl);
    }

    return "index";
}

    @PostMapping("/index")
    public String indexPost(
            @RequestParam(name = "inputAmt", required = false) Integer inputAmt,
            @RequestParam(name = "inputEmail", required = false) String inputEmail,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (inputAmt == null) {
            redirectAttributes.addFlashAttribute("error", "Amount is required.");
            return "redirect:/index";
        }

        if (authentication != null && authentication.getPrincipal() instanceof Saml2AuthenticatedPrincipal principal) {
            String source = studentProperties.getAttributeSource();

            String student_id = principal.getFirstAttribute("student_id");
                StudentNameInfo studentNameInfo = studentService.getStudentInfoById(student_id);
            StudentEmailInfo studentEmailInfo = studentService.getEmailById(student_id);
            StudentPhoneInfo studentPhoneInfo = studentService.getPhoneById(student_id);
            StudentAddressInfo studentAddressInfo = studentService.getAddressInfoById(student_id);

            String nationCode = studentAddressInfo.getNationCode();

            StudentBalanceInfo studentBalanceInfo = studentService.getBalanceById(student_id);
            StudentNationInfo studentNationInfo = studentService.getNationName(nationCode);

            String firstName = studentNameInfo.getFirstName();
            String lastName = studentNameInfo.getLastName();
            String email = studentEmailInfo.getEmail();
            String phoneNumber = studentPhoneInfo.getPhoneNumber();
            String areaCode = studentPhoneInfo.getAreaCode();
            String phone = 1 + areaCode + phoneNumber;
            String address1 = studentAddressInfo.getAddress1();
            String address2 = studentAddressInfo.getAddress2();
            String address = null;
            if (address2 != null && !address2.isBlank())
            {
                address = address1 + " " + address2;  // add a space between
            }
            else {
                address = address1;
            }
            String city = studentAddressInfo.getCity();
            String state = studentAddressInfo.getState();
            String zip = studentAddressInfo.getZip();
            String account_balance = studentBalanceInfo.getAccountBalance().toString();

            logger.info("Creating Flywire session for student_id: {}", student_id);
            String country = null;
            if(nationCode != null) {
                country = studentNationInfo.getNationName();
                logger.info("short country name: {}", country);
            }
            else
            {
                country = null;
            }
            if( country == null && nationCode == null) {
                country = "US"; // default to US if null
            }

            String termCode = studentBalanceInfo.getTermCode();

            flywireService.generateSession(
                    firstName, lastName, email, student_id,
                    account_balance, address, city, state, zip, country, phone, inputAmt, termCode, inputEmail
            );
        }

        // PRG Pattern: redirect after POST
        redirectAttributes.addFlashAttribute("success", "Payment session created.");
        return "redirect:/index";
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
}
