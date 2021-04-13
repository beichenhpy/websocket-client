package cn.beichenhpy.websocketclient;

import cn.beichenhpy.websocketclient.anno.SocketMapping;
import cn.beichenhpy.websocketclient.pojo.Content;
import cn.beichenhpy.websocketclient.pojo.Message;
import cn.beichenhpy.websocketclient.pojo.MsgQuery;
import cn.beichenhpy.websocketclient.utils.SpringContextUtils;
import com.alibaba.fastjson.JSON;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author beichenhpy
 * @version 0.2
 * <p>继承WebSocketClient,实现了对应的{@code onClose};{@code onOpen};{@code onMessage};{@code onError}
 * <p>在{@link #onOpen}中方法中，实现了对所有的{@link SocketMapping} 的注解的<var>path</var>和对应的<var>Method</var>
 * 进行注册，到变量{@code pathToMethodMap}中保存，具体实现，使用{@link Reflections} 新建反射，扫描所有的该注解的方法
 * <p>在{@link #onMessage} 方法中，进行传回值得解析，{@link Message}
 * <p>----------------------------待添加用户自定义------------------------------
 * <p>方法{@link #findMethodAndInvoke(String, MsgQuery)}为根据path查询并反射调用方法-单线程
 * <p>方法{@link #findMethodAndInvokePara(String, MsgQuery)} 为根据path查询并反射调用方法-并行
 * <p> 在{@link #onClose} 方法中，添加了失败后，多线程重试，通过 {@link #reconnect()}方法进行重试
 * @see SocketMapping
 * @see Reflections
 * @see Message
 * @since 2021/1/15 15:28 -update 2021/4/13
 */
public class WsClient extends WebSocketClient {
    private static final Logger log = LoggerFactory.getLogger(WsClient.class);
    protected final ConcurrentHashMap<String[], Method> pathToMethodMap = new ConcurrentHashMap<>();

    /**
     * 是否扫描过
     */
    private static boolean isScanned = false;
    /**
     * 线程池
     */
    private TaskExecutor taskExecutor;
    /**
     * 反射路径
     */
    private final String reflectionPath;
    /**
     * 睡眠时间 默认5s
     */
    private Long reconnectTime = 5000L;

    public WsClient(URI uri,String reflectionPath,Long reconnectTime) {
        super(uri);
        this.reconnectTime = reconnectTime;
        this.reflectionPath = reflectionPath;
        //单例初始化线程池
        if (taskExecutor == null){
            synchronized (WsClient.class){
                taskExecutor = initThreadPool();
            }
        }
        this.connect();
    }
    public WsClient(URI uri,String reflectionPath) {
        super(uri);
        this.reflectionPath = reflectionPath;
        //单例初始化线程池
        if (taskExecutor == null){
            synchronized (WsClient.class){
                taskExecutor = initThreadPool();
            }
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
        //执行扫描
        if (!isScanned){
            Reflections reflections = new Reflections(reflectionPath,new MethodAnnotationsScanner());
            Set<Method> typesAnnotatedWith = reflections.getMethodsAnnotatedWith(SocketMapping.class);
            for (Method method : typesAnnotatedWith) {
                String[] value = method.getDeclaredAnnotation(SocketMapping.class).value();
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
        //取出反射
        findMethodAndInvoke(path,query);
        log.info("[websocket] 收到消息={}", message);

    }

    /**
     * 并行寻找对应Method
     * @param path mapping映射路径
     * @param query 查询条件
     */
    private void findMethodAndInvokePara(String path,MsgQuery query){
        pathToMethodMap.forEachKey(1, strings -> {
            if (Arrays.asList(strings).contains(path)){
                invokeMethod(strings, query);
            }
        });
    }


    /**
     * 找到对应path的方法，并且根据query去反射调用
     * @param path 方法对应的mapping
     * @param query 查询条件
     */
    private void findMethodAndInvoke(String path,MsgQuery query){
        ConcurrentHashMap.KeySetView<String[], Method> keySetView = pathToMethodMap.keySet();
        for (String[] paths : keySetView) {
            if (Arrays.asList(paths).contains(path)){
                invokeMethod(paths, query);
                break;
            }
        }
    }

    /**
     * 反射执行方法
     * @param paths 获取方法
     * @param query 查询条件
     */
    private void invokeMethod(String[] paths, MsgQuery query) {
        Method method = pathToMethodMap.get(paths);
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            //拿到bean实例
            Object bean = SpringContextUtils.getBean(declaringClass);
            //反射
            if (query == null){
                method.invoke(bean);
            }else {
                method.invoke(bean,query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[websocket] 退出连接，code:{},reason:{},remote:{}",code,reason,remote );
        if (code == CloseFrame.NEVER_CONNECTED){
            log.warn("【webSocket】连接失败----服务器拒绝连接，请检查服务器连接配置或服务器是否存在");
        }
        if (code == CloseFrame.ABNORMAL_CLOSE){
            log.warn("【webSocket】退出连接----服务器主动关闭连接，请检查WebSocket服务器运行是否正常");
        }
        taskExecutor.execute(this::reconnect);
        try {
            Thread.sleep(reconnectTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception ex) {
        log.info("[websocket] 连接错误={},",ex.toString());
    }
}
