package com.example.demo;

import cn.beichenhpy.websocketclient.WsClient;
import cn.beichenhpy.websocketclient.anno.WebSocketMsg;
import cn.beichenhpy.websocketclient.config.WsClientProperties;
import cn.beichenhpy.websocketclient.pojo.MsgQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    private final WsClientProperties wsClientProperties;
    private final WsClient wsClient;
    public DemoApplication(WsClient wsClient,WsClientProperties wsClientProperties){
        this.wsClientProperties = wsClientProperties;
        this.wsClient = wsClient;
    }

    @WebSocketMsg("/test")
    public void test(MsgQuery msgQuery){
        System.out.println(msgQuery);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
