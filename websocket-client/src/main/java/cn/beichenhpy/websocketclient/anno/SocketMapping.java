package cn.beichenhpy.websocketclient.anno;

import java.lang.annotation.*;

/**
 * @author beichenhpy
 * @version 0.2
 * <p>为了能够按照websocket传输报文中指定的方法来执行，简化代码的注解.
 *
 * <p>websocket报文中对应的path {@link cn.beichenhpy.websocketclient.pojo.Content}.
 *
 * <p>具体实现方式在 {@link cn.beichenhpy.websocketclient.WsClient#onOpen}.
 *
 * @since 2021/1/20 10:30 -update 2021/4/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SocketMapping {
    /**
     * 使用方式
     * value = {"/path1","/path2"} / "path"
     * @return 返回String路径数组
     */
    String[] value() default {};
}
