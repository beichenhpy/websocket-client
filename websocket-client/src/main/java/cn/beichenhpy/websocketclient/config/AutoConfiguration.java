package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.utils.SpringContextUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;


/**
 * @author beichenhpy
 * @version 0.2
 * <p>autoConfig类，用于提供外部bean和properties
 * <p>提供对{@link SpringContextUtils}工具类的引入
 * <p>提供对外部使用自动配置类{@link WsClientProperties}
 * @since 2021/1/22 12:09 -update 2021/4/13
 */
@Configuration
@Import({SpringContextUtils.class})
@EnableConfigurationProperties({WsClientProperties.class})
public class AutoConfiguration {


}
