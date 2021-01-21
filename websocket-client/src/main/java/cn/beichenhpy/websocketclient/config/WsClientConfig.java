package cn.beichenhpy.websocketclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 配置websocket server uri
 * @since 2021/1/15 15:34
 */
@Configuration
public class WsClientConfig {
    @Value("${WebSocket.uri}")
    private String uri;
    @Bean
    URI uri() throws URISyntaxException {
        return new URI(uri);
    }
}
