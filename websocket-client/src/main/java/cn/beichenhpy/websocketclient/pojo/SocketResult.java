package cn.beichenhpy.websocketclient.pojo;


import cn.beichenhpy.websocketclient.constant.CommonConstant;

import java.io.Serializable;

/**
 * @author beichenhpy
 * @since 2021-1-15
 * @description TODO 查询结果实体类 可自行修改
 */
public class SocketResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;
    /**
     * 返回处理消息
     */
    private String message = "操作成功！";
    /**
     * 返回代码
     */
    private Integer code = 0;
    /**
     * 返回数据对象 data
     */
    private T result;
    public SocketResult() {

    }

    public SocketResult<T> success(String message) {
        this.message = message;
        this.code = CommonConstant.SC_OK_200;
        this.success = true;
        return this;
    }


    public static SocketResult<Object> broadcast(Object data) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setResult(data);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("广播成功");
        return r;
    }

    public static SocketResult<Object> ok(String msg) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage(msg);
        return r;
    }

    public static SocketResult<Object> ok(Object data) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static SocketResult<Object> error(String msg) {
        return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static SocketResult<Object> error(int code, String msg) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public SocketResult<T> error500(String message) {
        this.message = message;
        this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
        return this;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
