package com.doctums.banner.paymentgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml2.core.Saml2ResponseValidatorResult;
import org.springframework.security.saml2.provider.service.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    // Register your custom SAML2 provider globally
    @Bean
    public OpenSaml4AuthenticationProvider samlAuthenticationProvider() {
        System.out.println("[DEBUG] SecurityConfig - registering OpenSaml4AuthenticationProvider");
        OpenSaml4AuthenticationProvider provider = new OpenSaml4AuthenticationProvider();

        // Custom response validator (optional)
        provider.setResponseValidator(responseToken -> {
            Saml2ResponseValidatorResult result = OpenSaml4AuthenticationProvider
                    .createDefaultResponseValidator()
                    .convert(responseToken);

            if (result == null || result.hasErrors()) {
                LOG.error("SAML validation failed: {}", result.getErrors());
                String inResponseTo = responseToken.getResponse().getInResponseTo();
                throw new CustomSamlValidationException(inResponseTo);
            }

            LOG.info("SAML2 RESPONSE: {}", responseToken.getToken().getSaml2Response());
            return result;
        });

        // Custom authorities from SAML attributes
        provider.setResponseAuthenticationConverter(responseToken -> {
            Saml2Authentication auth = OpenSaml4AuthenticationProvider
                    .createDefaultResponseAuthenticationConverter()
                    .convert(responseToken);

            Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) auth.getPrincipal();

            List<GrantedAuthority> authorities = new ArrayList<>();
            List<Object> roles = principal.getAttribute("Role");

            if (roles != null) {
                for (Object role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase().replace("-", "_")));
                }
            }

            LOG.info("SAML user: {}, roles: {}", principal.getName(), authorities);
            return new Saml2Authentication(principal, auth.getSaml2Response(), authorities);
        });

        return provider;
    }

    // Register AuthenticationManager with your provider
    @Bean
    public AuthenticationManager authenticationManager(OpenSaml4AuthenticationProvider provider) {
        return new ProviderManager(provider);
    }

    // Define the success handler
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        System.out.println("[DEBUG] SecurityConfig - creating AuthenticationSuccessHandler bean");
        return (request, response, authentication) -> {
            System.out.println("AuthenticationSuccessHandler HIT! Redirecting to /index");
            LOG.info("SAML login successful. Redirecting to /index");
            response.sendRedirect("/index");
        };
    }

    // Main Spring Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http
//                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico",
                                "/api/flywire/webhook", "/api/flywire/session", "/api/flywire/payment-result", "/success.html", "/index.html", "/error", "/default-ui.css", "api/flywire/payments/**", "/success**", "/.well-known/appspecific/com.chrome.devtools.json", "/success", "/pay-now").permitAll()
                        .anyRequest().authenticated())
                .saml2Login(saml2 -> saml2
                        .authenticationManager(authManager)
                        .successHandler(successHandler())
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/flywire/webhook", "/api/flywire/session", "/api/flywire/payment-result", "/success.html", "/index.html", "api/flywire/payments/**", "/success**", "/.well-known/appspecific/com.chrome.devtools.json", "/success", "/pay-now") // Disable CSRF for specific endpoints
                )
                .saml2Metadata(withDefaults());

        return http.build();
    }
}
