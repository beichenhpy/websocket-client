package cn.beichenhpy.websocketclient.config;

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

    private final WsClientYmlConfig wsClientYmlConfig;

    public WsClientConfig(WsClientYmlConfig wsClientYmlConfig) {
        this.wsClientYmlConfig = wsClientYmlConfig;
    }

    @Bean
    URI uri() throws URISyntaxException {
        //todo 未来将会添加token生成验证
        return new URI(wsClientYmlConfig.getWebSocketServerUri());
    }
}
