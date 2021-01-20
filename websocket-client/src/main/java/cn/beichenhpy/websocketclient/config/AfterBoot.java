package cn.beichenhpy.websocketclient.config;

import cn.beichenhpy.websocketclient.anno.WebSocketMsg;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO springboot 启动后执行 扫描所有带有注解 WebSocketMsg的 添加到Map中
 * @since 2021/1/15 15:39
 */
@Component
public class AfterBoot implements ApplicationRunner {
    public  ConcurrentHashMap<String[], Method> pathToMethodMap = new ConcurrentHashMap<>();

    public  ConcurrentHashMap<String[], Method> getPathToMethodMap() {
        return pathToMethodMap;
    }

    @Override
    public void run(ApplicationArguments args) {
        Reflections reflections = new Reflections("cn.beichenhpy",new MethodAnnotationsScanner());
        Set<Method> typesAnnotatedWith = reflections.getMethodsAnnotatedWith(WebSocketMsg.class);
        for (Method method : typesAnnotatedWith) {
            String[] value = method.getDeclaredAnnotation(WebSocketMsg.class).value();
            pathToMethodMap.put(value,method);
        }
    }
}
