package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.WsClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/22 12:09
 */
@Configuration
@ConditionalOnClass({WsClient.class})
@EnableConfigurationProperties({ WsClientYmlConfig.class })
public class AutoConfiguration {

    private final WsClientYmlConfig wsClientYmlConfig;

    public AutoConfiguration(WsClientYmlConfig wsClientYmlConfig){
        this.wsClientYmlConfig = wsClientYmlConfig;
    }

    @Bean
    @ConditionalOnMissingBean(WsClient.class)
    public WsClient wsClient() throws URISyntaxException {
        URI uri = new URI(wsClientYmlConfig.getWebSocketServerUri());
        //TODO 将会使用JwtUtil增加token验证机制
        return new WsClient(uri,wsClientYmlConfig);
    }


}
