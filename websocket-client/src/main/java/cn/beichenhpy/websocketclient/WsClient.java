package cn.beichenhpy.websocketclient;

import cn.beichenhpy.websocketclient.anno.WebSocketMsg;
import cn.beichenhpy.websocketclient.config.WsClientYmlConfig;
import cn.beichenhpy.websocketclient.pojo.Content;
import cn.beichenhpy.websocketclient.pojo.Message;
import cn.beichenhpy.websocketclient.pojo.MsgQuery;
import cn.beichenhpy.websocketclient.utils.SpringContextUtils;
import com.alibaba.fastjson.JSON;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO websocket客户端
 * @since 2021/1/15 15:28
 */
@Component
public class WsClient extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger(WsClient.class);
    public  ConcurrentHashMap<String[], Method> pathToMethodMap = new ConcurrentHashMap<>();

    /**
     * repeatTime 尝试次数，超过5次则先暂停尝试
     * notHeartBeatTime 未尝试次数，等于50后重置repeatTime
     */
    private int repeatTime = 5;
    private int notHeartBeatTime = 0;
    /**
     * 连接成功过一次后设置为true
     */
    public boolean wasConnected = false;
    /**
     * 是否扫描过
     */
    private boolean isScanned = false;

    @Autowired
    private  WsClientYmlConfig wsClientYmlConfig;



    public WsClient(URI uri) {
        super(uri);
        this.connect();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[websocket] 连接成功");
        wasConnected = true;
        //执行扫描
        if (!isScanned){
            Reflections reflections = new Reflections(wsClientYmlConfig.getReflectionPath(),new MethodAnnotationsScanner());
            Set<Method> typesAnnotatedWith = reflections.getMethodsAnnotatedWith(WebSocketMsg.class);
            for (Method method : typesAnnotatedWith) {
                String[] value = method.getDeclaredAnnotation(WebSocketMsg.class).value();
                pathToMethodMap.put(value,method);
            }
            isScanned = true;
        }
    }

    @Override
    public void onMessage(String message) {
        //解析传送Message
        Message msg = JSON.parseObject(message, Message.class);
        //拿到content
        Content content = JSON.parseObject(msg.getContent(), Content.class);
        //拿到路径和query条件
        String path = content.getPath();
        MsgQuery query = content.getMsgQuery();
        //取出keySet
        ConcurrentHashMap.KeySetView<String[], Method> keySetView = pathToMethodMap.keySet();
        for (String[] strings : keySetView) {
            for (String string : strings) {
                if (path.equals(string)){
                    Method method = pathToMethodMap.get(strings);
                    try {
                        Class<?> declaringClass = method.getDeclaringClass();
                        //避免多例 尝试用spring获取对象
                        //拿到类名 开头转小写
                        String simpleName = declaringClass.getSimpleName();
                        simpleName = simpleName.substring(0,1).toLowerCase(Locale.ROOT).concat(simpleName.substring(1));
                        //拿到bean实例
                        Object bean = SpringContextUtils.getBean(simpleName);
                        //反射
                        if (query == null){
                            method.invoke(bean);
                        }else {
                            method.invoke(bean,query);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        log.info("[websocket] 收到消息={}", message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[websocket] 退出连接，{}", reason);
    }

    @Override
    public void onError(Exception ex) {
        log.info("[websocket] 连接错误={}", ex.getMessage());
    }

    @Scheduled(cron = "${ws-client.heartbeat}")
    public void heartBeat() {
        log.info("【心跳线程执行】:当前client连接状态为：{}", this.getReadyState());
        if (!this.wasConnected){
            /*第一次连接是否成功：false证明已经尝试过且失败 那么则状态已经为closed 需要reconnect*/
            log.warn("[websocket初次连接失败]---正在努力尝试");
            this.reconnect();
        } else if (this.isClosed() || this.isClosing()) {
            //用来判断连接不上服务器的情况的
            if (repeatTime != 0) {
                log.warn("[websocket服务器连接失败]----正在尝试重新连接（剩余尝试次数：{}次）", repeatTime - 1);
                this.reconnect();
                repeatTime--;
            }
        }
        if (repeatTime == 0) {
            notHeartBeatTime++;
        }
        if (notHeartBeatTime == 50) {
            repeatTime = 5;
        }

    }
}
