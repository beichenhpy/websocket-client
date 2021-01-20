package cn.beichenhpy.websocketclient.api.service.Impl;

import cn.beichenhpy.websocketclient.api.service.DeviceScreenService;
import org.springframework.stereotype.Service;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/20 10:15
 */
@Service
public class DeviceScreenServiceImpl implements DeviceScreenService {
    @Override
    public String listGroupWork() {
        return "ok";
    }
}
