package cn.beichenhpy.websocketclient.pojo;

import java.io.Serializable;

/**
 * @author beichenhpy
 * @version 1.0
 * @description TODO 查询条件 queryContent需要根据queryType进行反序列化
 * @since 2021/1/22 10:08
 */
public class MsgQuery implements Serializable {
    /**
     * 查询内容，序列化过的，需要根据queryType进行实体类转换，可以通过enum来做对应
     */
    private String queryContent;
    /**
     * queryType 查询类型
     */
    private String queryType;

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    @Override
    public String toString() {
        return "MsgQuery{" +
                "queryContent='" + queryContent + '\'' +
                ", queryType='" + queryType + '\'' +
                '}';
    }
}
