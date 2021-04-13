package cn.beichenhpy.websocketclient.pojo;

import java.io.Serializable;

/**
 * @author beichenhpy
 * @version 1.0
 * 具体的传输内容
 * @since 2021/1/22 10:15
 */
public class Content implements Serializable {
    private String path;
    private MsgQuery msgQuery;
    private SocketResult<?> socketResult;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MsgQuery getMsgQuery() {
        return msgQuery;
    }

    public void setMsgQuery(MsgQuery msgQuery) {
        this.msgQuery = msgQuery;
    }

    public SocketResult<?> getSocketResult() {
        return socketResult;
    }

    public void setSocketResult(SocketResult<?> socketResult) {
        this.socketResult = socketResult;
    }
}
