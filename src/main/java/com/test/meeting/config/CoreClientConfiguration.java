package com.test.meeting.config;

import com.test.meeting.client.ValidatorClient;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreClientConfiguration {

    @Value("${application.client.endpoint.validator}")
    private String url;
    private Client client;

    @Autowired
    public CoreClientConfiguration(Client client) {
        this.client = client;
    }

    @Bean
    ValidatorClient getValidatorClient() {
        return Feign.builder()
                .client(client)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(ValidatorClient.class))
                .logLevel(Logger.Level.HEADERS)
                .retryer(Retryer.NEVER_RETRY)
                .target(ValidatorClient.class, url);
    }
}
