package cn.beichenhpy.websocket.pojo;


import cn.beichenhpy.websocket.constant.CommonConstant;

public class SocketResult<T> {

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
     * 广播组：对消息进行分组
     */
    private String section = "0";


    private SocketQuery param = new SocketQuery();


    /**
     * 返回代码
     */
    private String methodName = "";

    /**
     * 返回数据对象 data
     */
    private T socketResult;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public SocketResult() {

    }

    public SocketResult<T> success(String message,String methodName) {
        this.message = message;
        this.code = CommonConstant.SC_OK_200;
        this.methodName = methodName;
        this.success = true;
        return this;
    }


    public static SocketResult<Object> ok(String methodName) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setMethodName(methodName);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("成功");
        return r;
    }

    public static SocketResult<Object> broadcast(Object data,String methodName,String section) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setMethodName(methodName);
        r.setSocketResult(data);
        r.setSection(section);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("广播成功");
        return r;
    }

    public static SocketResult<Object> broadcast(Object data,String methodName,String section,SocketQuery param) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setMethodName(methodName);
        r.setSocketResult(data);
        r.setSection(section);
        r.setParam(param);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("带参广播成功");
        return r;
    }

    public static SocketResult<Object> ok(String msg,String methodName) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setMethodName(methodName);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage(msg);
        return r;
    }

    public static SocketResult<Object> ok(Object data,String methodName) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setSuccess(true);
        r.setMethodName(methodName);
        r.setCode(CommonConstant.SC_OK_200);
        r.setSocketResult(data);
        return r;
    }

    public static SocketResult<Object> error(String msg,String methodName) {
        return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg,methodName);
    }

    public static SocketResult<Object> error(int code, String msg,String methodName) {
        SocketResult<Object> r = new SocketResult<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setMethodName(methodName);
        r.setSuccess(false);
        return r;
    }

    public SocketResult<T> error500(String message,String methodName) {
        this.message = message;
        this.methodName = methodName;
        this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
        return this;
    }
    /**
     * 无权限访问返回结果
     */
    public static SocketResult<Object> noauth(String msg,String methodName) {
        return error(CommonConstant.SC_JEECG_NO_AUTHZ, msg,methodName);
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public SocketQuery getParam() {
        return param;
    }

    public void setParam(SocketQuery param) {
        this.param = param;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public T getSocketResult() {
        return socketResult;
    }

    public void setSocketResult(T socketResult) {
        this.socketResult = socketResult;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
