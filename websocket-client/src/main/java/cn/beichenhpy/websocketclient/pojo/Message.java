package cn.beichenhpy.websocketclient.pojo;

import java.io.Serializable;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 服务端消息类，根据这个反序列化
 * @since 2021/1/15 16:39
 */
public class Message implements Serializable {
    /**
     * 发消息人
     */
    private String fromUser;
    /**
     * 收消息人
     */
    private String toUser;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型
     */
    private String msgType;

    public Message(String fromUser, String toUser, String content, String msgType) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.msgType = msgType;
    }

    public Message() {
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", content='" + content + '\'' +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
