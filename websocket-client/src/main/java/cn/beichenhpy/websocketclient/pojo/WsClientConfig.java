package cn.beichenhpy.websocketclient.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/21 13:46
 */
@ConfigurationProperties(prefix = "ws-client")
public class WsClientConfig {
    private String heartbeat;
    private String reflectionPath;
    private String webSocketServerUri;

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
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
