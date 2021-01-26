package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.WsClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.net.URI;


/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO autoConfig类，用于提供外部bean和properties
 * @since 2021/1/22 12:09
 */
@Configuration
@Import(WsClientUriConfig.class)
@ConditionalOnClass({WsClient.class})
@EnableConfigurationProperties({ WsClientProperties.class })
public class AutoConfiguration {

    private final WsClientProperties wsClientProperties;
    private final URI uri;

    public AutoConfiguration(WsClientProperties wsClientProperties, URI uri){
        this.wsClientProperties = wsClientProperties;
        this.uri = uri;
    }

    @Bean
    @ConditionalOnMissingBean(WsClient.class)
    public WsClient wsClient() {
        return new WsClient(uri, wsClientProperties);
    }


}
