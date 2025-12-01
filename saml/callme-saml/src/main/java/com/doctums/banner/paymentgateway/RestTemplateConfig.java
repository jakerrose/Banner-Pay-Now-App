package com.doctums.banner.paymentgateway;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class RestTemplateConfig {

    private final ProxyProperties proxyProperties;

    @Autowired
    public RestTemplateConfig(ProxyProperties proxyProperties) {
        this.proxyProperties = proxyProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        // Debug: report proxy configuration and indicate creation of RestTemplate bean
        System.out.println("[DEBUG] RestTemplateConfig - creating RestTemplate bean. proxy.enabled=" + proxyProperties.isEnabled());

        HttpClient httpClient;

        if (proxyProperties.isEnabled()) {
            System.out.println("[DEBUG] RestTemplateConfig - proxy host=" + proxyProperties.getHost() + ", port=" + proxyProperties.getPort());
            HttpHost proxy = new HttpHost(proxyProperties.getHost(), proxyProperties.getPort());
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

            httpClient = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        RestTemplate rt = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        System.out.println("[DEBUG] RestTemplateConfig - RestTemplate bean created");
        return rt;
    }
}
