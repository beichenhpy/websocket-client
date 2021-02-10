package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.WsClient;
import cn.beichenhpy.websocketclient.utils.SpringContextUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO autoConfig类，用于提供外部bean和properties
 * @since 2021/1/22 12:09
 */
@Configuration
@Import({SpringContextUtils.class})
@EnableConfigurationProperties({WsClientProperties.class})
public class AutoConfiguration {


}
