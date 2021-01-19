package cn.beichenhpy.websocketclient.pojo;

import java.io.Serializable;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/15 16:39
 */
public class Message implements Serializable {
    private String fromUser;
    private String toUser;
    private SocketResult<?> socketResult;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public SocketResult<?> getSocketResult() {
        return socketResult;
    }

    public void setSocketResult(SocketResult<?> socketResult) {
        this.socketResult = socketResult;
    }
}
