package cn.beichenhpy.websocketclient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO springboot 启动后执行 因使用定时任务暂时不用
 * @since 2021/1/15 15:39
 */
@Component
@Deprecated
public class AfterBoot implements ApplicationRunner {
    @Autowired
    private WsClient wsClient;
    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
    @Override
    public void run(ApplicationArguments args) {
        //taskExecutor.execute(new HeartThread(wsClient));
    }
}
