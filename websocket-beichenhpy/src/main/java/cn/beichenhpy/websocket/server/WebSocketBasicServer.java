package cn.beichenhpy.websocket.server;

import cn.beichenhpy.websocket.pojo.Message;
import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/15 12:32
 */
@ServerEndpoint(path = "/beichenhpy/ws/{deviceNo}",port = "${server.port}")
public class WebSocketBasicServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketBasicServer.class);
    private static ConcurrentHashMap<String, Session> clientsMap = new ConcurrentHashMap<>();
    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        session.setSubprotocols("stomp");
        if (!"ok".equals(req)){
            log.error("授权失败");
            session.close();
        }
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String req, @RequestParam MultiValueMap reqMap, @PathVariable String arg, @PathVariable Map pathMap){
        log.info("连接成功,session:{}",session);
        String deviceNo = pathMap.get("deviceNo").toString();
        clientsMap.put(deviceNo,session);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        for(Map.Entry<String,Session> entry:clientsMap.entrySet()){
            if (entry.getValue().equals(session)){
                clientsMap.remove(entry.getKey());
                log.info("断开连接,session:{}",session);
                break;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("发送消息");
        Message messageJson = JSON.parseObject(message, Message.class);
        log.info("消息内容：{}",messageJson.toString());
        sendInfo(messageJson.getToUser(),message);
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }
    //给指定用户发送信息
    public void sendInfo(String userName, String message){
        Session session = clientsMap.get(userName);
        try {
            session.sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
