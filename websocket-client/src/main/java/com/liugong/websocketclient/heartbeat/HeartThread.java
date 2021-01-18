package com.liugong.websocketclient.heartbeat;

import com.liugong.websocketclient.config.WsClient;
import com.liugong.websocketclient.utils.SpringContextUtils;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/18 9:14
 */
public class HeartThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HeartThread.class);
    /**
     * repeatTime 尝试次数，超过5次则先暂停尝试
     * notHeartBeatTime 未尝试次数，等于200后重置repeatTime
     */
    private int repeatTime = 6;
    private int notHeartBeatTime = 0;
    WsClient wsClient;

    public HeartThread(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    @Override
    public void run() {
        while (true) {
            log.info(Thread.currentThread().getName() + "【心跳线程执行】");
            if (wsClient.isClosed() && repeatTime != 0) {
                log.info(Thread.currentThread().getName() + "[websocket服务器连接失败]----正在尝试重新连接（剩余尝试次数：{}次）", repeatTime - 1);
                wsClient = (WsClient) SpringContextUtils.getBean("wsClient");
                wsClient.connect();
                repeatTime --;
            }
            try {
                Thread.sleep(10000);
                if (repeatTime == 0){
                    notHeartBeatTime ++;
                }
                if (notHeartBeatTime == 50){
                    repeatTime = 6;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
