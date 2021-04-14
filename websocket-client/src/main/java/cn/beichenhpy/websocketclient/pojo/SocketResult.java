package cn.beichenhpy.websocketclient.pojo;



import java.io.Serializable;

/**
 * @author beichenhpy
 * @since 2021-1-15
 * 查询结果实体类 可自行修改
 */
public class SocketResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;
    /**
     * 返回处理消息
     */
    private String message;
    /**
     * 返回代码
     */
    private Integer code;
    /**
     * 返回数据对象 data
     */
    private Object result;
    public SocketResult() {

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
