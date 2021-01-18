package com.liugong.websocketclient.config;

import com.liugong.websocketclient.heartbeat.HeartThread;
import com.liugong.websocketclient.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO springboot 启动后执行 连接websocket服务器和心跳服务
 * @since 2021/1/15 15:39
 */
@Component
public class AfterBoot implements ApplicationRunner {
    @Autowired
    private WsClient wsClient;
    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
    @Override
    public void run(ApplicationArguments args) {
        taskExecutor.execute(new HeartThread(wsClient));
    }
}
