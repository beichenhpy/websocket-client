package cn.beichenhpy.websocketclient.heartbeat;

import cn.beichenhpy.websocketclient.config.WsClient;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 心跳线程
 * @since 2021/1/18 9:14
 */
public class HeartThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HeartThread.class);
    /**
     * repeatTime 尝试次数，超过5次则先暂停尝试
     * notHeartBeatTime 未尝试次数，等于200后重置repeatTime
     */
    private int repeatTime = 5;
    private int notHeartBeatTime = 0;
    WsClient wsClient;

    public HeartThread(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    @Override
    public void run() {
        while (true) {
            log.info(Thread.currentThread().getName() + "【心跳线程执行】:当前client连接状态为：{}", wsClient.getReadyState());
            if (wsClient.getReadyState().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED)) {
                wsClient.connect();
            } else if (wsClient.isClosed() || wsClient.isClosing()) {
                if(repeatTime != 0){
                    log.info(Thread.currentThread().getName() + "[websocket服务器连接失败]----正在尝试重新连接（剩余尝试次数：{}次）", repeatTime - 1);
                    wsClient.reconnect();
                    repeatTime--;
                }
            }
            try {
                Thread.sleep(10000);
                if (repeatTime == 0) {
                    notHeartBeatTime++;
                }
                if (notHeartBeatTime == 50) {
                    repeatTime = 5;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
