package cn.beichenhpy.websocketclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/21 13:46
 */
@Component
@ConfigurationProperties(prefix = "ws-client")
public class WsClientProperties {
    /**
     * 重连时间间隔 cron表达式
     */
    private Long reconnectTime;
    /**
     * 反射搜索路径
     */
    private String reflectionPath;
    /**
     * webSocketServer连接
     */
    private String webSocketServerUri;

    public Long getReconnectTime() {
        return reconnectTime;
    }

    public void setReconnectTime(Long reconnectTime) {
        this.reconnectTime = reconnectTime;
    }

    public String getReflectionPath() {
        return reflectionPath;
    }

    public void setReflectionPath(String reflectionPath) {
        this.reflectionPath = reflectionPath;
    }

    public String getWebSocketServerUri() {
        return webSocketServerUri;
    }

    public void setWebSocketServerUri(String webSocketServerUri) {
        this.webSocketServerUri = webSocketServerUri;
    }
}
