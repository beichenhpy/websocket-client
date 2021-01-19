package cn.beichenhpy.websocketclient.Scheduling;

import cn.beichenhpy.websocketclient.config.WsClient;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/19 11:18
 */
@Component
public class HeartBeat {
    private static final Logger log = LoggerFactory.getLogger(HeartBeat.class);
    /**
     * repeatTime 尝试次数，超过5次则先暂停尝试
     * notHeartBeatTime 未尝试次数，等于200后重置repeatTime
     */
    private int repeatTime = 5;
    private int notHeartBeatTime = 0;
    @Autowired
    private WsClient wsClient;

    @Scheduled(cron = "${heartbeat.time}")
    public void heartBeat() {
        log.info("【心跳线程执行】:当前client连接状态为：{}", wsClient.getReadyState());
        if (wsClient.getReadyState().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED)) {
                wsClient.connect();
        } else if (!wsClient.wasConnected){
            //是否连接过，如果没链接过则执行
            log.warn("[websocket服务器初次连接失败]---正在努力尝试");
            wsClient.reconnect();
        } else if (wsClient.isClosed() || wsClient.isClosing()) {
            //如果连接过执行这里
            if (repeatTime != 0) {
                log.warn("[websocket服务器连接失败]----正在尝试重新连接（剩余尝试次数：{}次）", repeatTime - 1);
                wsClient.reconnect();
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
