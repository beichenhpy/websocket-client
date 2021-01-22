package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.pojo.Message;
import cn.beichenhpy.websocketclient.pojo.SocketQuery;
import cn.beichenhpy.websocketclient.utils.SpringContextUtils;
import com.alibaba.fastjson.JSON;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Locale;
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
    @Autowired
    private AfterBoot afterBoot;
    /**
     * 连接成功过一次后设置为true
     */
    public boolean wasConnected = false;


    public WsClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("[websocket] 连接成功");
        wasConnected = true;
    }

    @Override
    public void onMessage(String message) {
        Message messageJson = JSON.parseObject(message, Message.class);
        String path = messageJson.getPath();
        SocketQuery query = messageJson.getQuery();
        //todo 利用methodName反射执行对应实现类的方法 单纯的enum管理反射不好，失去了多态特性，如果使用path来匹配的话可能会好一些
        ConcurrentHashMap<String[], Method> pathToMethodMap = afterBoot.getPathToMethodMap();
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
                        method.invoke(bean,query);
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
}
