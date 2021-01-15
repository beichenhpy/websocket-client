package com.liugong.websocketclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/15 15:34
 */
@Configuration
public class WsClientConfig {
    @Bean
    URI uri() throws URISyntaxException {
        return new URI("ws://localhost:9999/beichenhpy/ws/backend?req=ok");
    }
}
