package cn.beichenhpy.websocketclient.api.service.Impl;

import cn.beichenhpy.websocketclient.api.service.DeviceScreenService;
import cn.beichenhpy.websocketclient.config.WsClient;
import cn.beichenhpy.websocketclient.pojo.Message;
import cn.beichenhpy.websocketclient.pojo.SocketQuery;
import cn.beichenhpy.websocketclient.pojo.SocketResult;
import com.alibaba.fastjson.JSON;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/20 10:15
 */
@Service
public class DeviceScreenServiceImpl implements DeviceScreenService {
    @Autowired
    private WsClient wsClient;
    @Override
    public String listGroupWork() {
        Message message = new Message();
        message.setFromUser("backend");
        message.setToUser("bigscreen");
        message.setPath(null);
        message.setQuery(new SocketQuery("0","day"));
        message.setSocketResult(SocketResult.ok("listGroupWork"));
        wsClient.send(JSON.toJSONString(message));
        return "ok";
    }
}
