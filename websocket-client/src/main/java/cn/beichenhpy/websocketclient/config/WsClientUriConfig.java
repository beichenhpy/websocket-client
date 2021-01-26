package cn.beichenhpy.websocketclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 提供URI bean
 * @since 2021/1/26 10:32
 */
@Configuration
public class WsClientUriConfig {

    private final WsClientProperties wsClientProperties;

    public WsClientUriConfig(WsClientProperties wsClientProperties){
        this.wsClientProperties = wsClientProperties;
    }

    @Bean
    public URI uri() throws URISyntaxException {
        //todo 加上token验证
        return new URI(wsClientProperties.getWebSocketServerUri());
    }
}
