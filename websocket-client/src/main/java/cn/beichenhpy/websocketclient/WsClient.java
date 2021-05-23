package cn.beichenhpy.websocketclient;

import cn.beichenhpy.websocketclient.anno.SocketMapping;
import cn.beichenhpy.websocketclient.pojo.Content;
import cn.beichenhpy.websocketclient.pojo.Message;
import cn.beichenhpy.websocketclient.pojo.MsgQuery;
import cn.beichenhpy.websocketclient.pojo.SocketResult;
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
import org.springframework.util.Assert;
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
 * <p>内部类{@link CastUtils#cast(Object)} 用于强制转换，
 * 参考 <a href="https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/util/CastUtils.java">CastUtils</a>
 * <p>方法{@link #findMethodAndInvoke(String, MsgQuery)}为根据path查询并反射调用方法
 * <p>在{@link #onClose} 方法中，添加了失败后，多线程重试，通过 {@link #reconnect()}方法进行重试
 * <p>{@link #sendToCenter(Object, Message)} 将执行结果发送回消息中心
 * @see SocketMapping
 * @see Reflections
 * @see Message
 * @since 2021/1/15 15:28 -update 2021/5/23
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
        log.info("[websocket] connect success");
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
        Assert.notNull(message,"message must not be null");
        //拿到content
        Content content = JSON.parseObject(msg.getContent(), Content.class);
        Assert.notNull(content,"content must not be null");
        //拿到路径和query条件
        String path = content.getPath();
        MsgQuery query = content.getMsgQuery();
        //取出反射
        Object result = findMethodAndInvoke(path, query);
        if (result != null){
            //发送到消息中心
            sendToCenter(result,msg);
        }
    }


    /**
     * 找到对应path的方法，并且根据query去反射调用
     * @param path 方法对应的mapping
     * @param query 查询条件
     */
    private Object findMethodAndInvoke(String path,MsgQuery query){
        ConcurrentHashMap.KeySetView<String[], Method> keySetView = pathToMethodMap.keySet();
        Object result = null;
        for (String[] paths : keySetView) {
            if (Arrays.asList(paths).contains(path)){
                result = invokeMethod(paths, query);
                break;
            }
        }
        return result;
    }

    /**
     * 反射执行方法
     * @param paths 获取方法
     * @param query 查询条件
     */
    private Object invokeMethod(String[] paths, MsgQuery query) {
        Object result = null;
        Method method = pathToMethodMap.get(paths);
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            //拿到bean实例
            Object bean = SpringContextUtils.getBean(declaringClass);
            //反射
            if (query == null){
                result = method.invoke(bean);
            }else {
                result = method.invoke(bean, query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("[websocket] 退出连接，code:{},reason:{},remote:{}",code,reason,remote );
        if (code == CloseFrame.NEVER_CONNECTED){
            log.warn("[webSocket]connect has refused----check url is available");
        }
        if (code == CloseFrame.ABNORMAL_CLOSE){
            log.warn("[webSocket] exit connection----websocket server has closed");
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
        log.info("[websocket] connection error={},",ex.toString());
    }

    /**
     * 将执行结果发送给消息中心 转发
     * @param result 结果
     * @param message 消息
     */
    public void sendToCenter(Object result,Message message){
        //转换
        SocketResult socketResult = CastUtils.cast(result);
        //设置内容
        Content content= JSON.parseObject(message.getContent(), Content.class);
        content.setSocketResult(socketResult);
        message.setContent(JSON.toJSONString(content));
        //互换发送方
        message.setFromUser(message.getToUser());
        message.setToUser(message.getFromUser());
        //发送
        this.send(JSON.toJSONString(message));
    }

   public interface CastUtils{
       /**
        * 转换
        * @param object source
        * @param <T> target
        * @return target
        */
       @SuppressWarnings("unchecked")
       static <T> T cast(Object object) {
           return (T) object;
       }
   }

}
