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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO websocket客户端
 * @since 2021/1/15 15:28
 */
@Component
public class WsClient extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger(WsClient.class);
    public static ConcurrentHashMap<String[], Method> pathToMethodMap = new ConcurrentHashMap<>();

    /**
     * repeatTime 尝试次数，超过5次则先暂停尝试
     * notHeartBeatTime 未尝试次数，等于50后重置repeatTime
     */
    private static int repeatTime = 5;
    private static int notHeartBeatTime = 0;
    /**
     * 连接成功过一次后设置为true
     */
    public static boolean wasConnected = false;
    /**
     * 是否扫描过
     */
    private static boolean isScanned = false;
    /**
     * 线程池
     */
    private static TaskExecutor taskExecutor;

    /**
     * 必须构造函数注入，因为自动注入会在构造函数执行之后才注入，会出现注入bean = null
     */
    private final WsClientYmlConfig wsClientYmlConfig;

    public WsClient(URI uri,WsClientYmlConfig wsClientYmlConfig) {
        super(uri);
        this.wsClientYmlConfig = wsClientYmlConfig;
        //单例初始化线程池
        if (taskExecutor == null){
            taskExecutor = initThreadPool();
        }
        this.connect();
    }

    /**
     * 初始化线程池
     * @return TaskExecutor
     */
    private TaskExecutor initThreadPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(50);
        // 设置最大线程数
        executor.setMaxPoolSize(100);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("wsClient-reconnect-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
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
        log.info("[websocket] 退出连接，code:{},reason:{},remote:{}",code,reason,remote );
        taskExecutor.execute(this::timingReconnect);
        try {
            Thread.sleep(wsClientYmlConfig.getReconnectTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception ex) {
        log.info("[websocket] 连接错误={},",ex.toString());
    }

    /**
     * 重连算法
     */
    public void timingReconnect() {
        log.info("【自动重连线程执行中】:当前client连接状态为：{}", this.getReadyState());
        if (!wasConnected){
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
