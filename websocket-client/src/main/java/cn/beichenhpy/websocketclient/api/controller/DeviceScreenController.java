package cn.beichenhpy.websocketclient.api.controller;

import cn.beichenhpy.websocketclient.anno.WebSocketMsg;
import cn.beichenhpy.websocketclient.api.service.DeviceScreenService;
import cn.beichenhpy.websocketclient.pojo.SocketQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 测试接口
 * @since 2021/1/20 10:46
 */
@Component
public class DeviceScreenController {
    @Autowired
    private DeviceScreenService deviceScreenService;

    @WebSocketMsg("listGroupWork")
    public void test(SocketQuery query){
        System.out.println(query);
        System.out.println(deviceScreenService.listGroupWork());
    }
}
