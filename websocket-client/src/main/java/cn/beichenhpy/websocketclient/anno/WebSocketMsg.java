package cn.beichenhpy.websocketclient.anno;

import java.lang.annotation.*;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO
 * @since 2021/1/20 10:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketMsg {
    String[] value() default {};
}
