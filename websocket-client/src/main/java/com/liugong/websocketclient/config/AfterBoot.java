package com.liugong.websocketclient.config;

import com.liugong.websocketclient.heartbeat.HeartThread;
import com.liugong.websocketclient.utils.SpringContextUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/15 15:39
 */
@Component
public class AfterBoot implements ApplicationRunner {

    WsClient wsClient;
    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
    @Override
    public void run(ApplicationArguments args) {
        wsClient= (WsClient) SpringContextUtils.getBean("wsClient");
        wsClient.connect();
        taskExecutor.execute(new HeartThread(wsClient));
    }
}
