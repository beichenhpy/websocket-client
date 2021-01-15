package com.liugong.websocketclient.config;

import com.liugong.websocketclient.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/15 15:39
 */
@Component
public class AfterBoot implements ApplicationRunner {

    WsClient wsClient;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        wsClient= (WsClient) SpringContextUtils.getBean("wsClient");
        wsClient.connect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (wsClient.isClosed()){
                        System.out.println("----------?do reconnect");
                        System.out.println(wsClient.toString());
                        wsClient= (WsClient) SpringContextUtils.getBean("wsClient");
                        wsClient.connect();
                    }
                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
