package cn.beichenhpy.websocketclient.heartbeat;

import cn.beichenhpy.websocketclient.WsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 定时心跳重连
 * @since 2021/1/19 11:18
 */
@Component
public class HeartBeat {
    private static final Logger log = LoggerFactory.getLogger(HeartBeat.class);
    /**
     * repeatTime 尝试次数，超过5次则先暂停尝试
     * notHeartBeatTime 未尝试次数，等于50后重置repeatTime
     */
    private int repeatTime = 5;
    private int notHeartBeatTime = 0;
    private final WsClient wsClient;

    public HeartBeat(WsClient wsClient){
        this.wsClient = wsClient;
        //初始化连接
        wsClient.connect();

    }

    @Scheduled(cron = "${ws-client.heartbeat}")
    public void heartBeat() {
        log.info("【心跳线程执行】:当前client连接状态为：{}", wsClient.getReadyState());
        if (!wsClient.wasConnected){
            /*第一次连接是否成功：false证明已经尝试过且失败 那么则状态已经为closed 需要reconnect*/
            log.warn("[websocket初次连接失败]---正在努力尝试");
            wsClient.reconnect();
        } else if (wsClient.isClosed() || wsClient.isClosing()) {
            //用来判断连接不上服务器的情况的
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
