package com.test.meeting.config;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Set configurations for Feign library
 */
@Configuration
public class FeignConfiguration {

    /**
     * Create a Okhttp Client to be instrumentalized by sleuth
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
