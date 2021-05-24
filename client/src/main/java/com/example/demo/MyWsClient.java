package com.example.demo;

import cn.beichenhpy.websocketclient.WsClient;
import cn.beichenhpy.websocketclient.config.WsClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class MyWsClient {
    private final WsClientProperties wsClientProperties;
    public MyWsClient(WsClientProperties wsClientProperties){
        this.wsClientProperties = wsClientProperties;
    }
    @Bean
    public WsClient wsClient() throws URISyntaxException {
        String webSocketServerUri = wsClientProperties.getWebSocketServerUri();
        String token = "?req=yes";
        String newUri = webSocketServerUri + token;
        return new WsClient(new URI(newUri),wsClientProperties.getReflectionPath(),wsClientProperties.getReconnectTime());
    }

}
