package cn.beichenhpy.websocketclient.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/22 12:09
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties({ WsClientYmlConfig.class })
public class AutoConfiguration {

}
