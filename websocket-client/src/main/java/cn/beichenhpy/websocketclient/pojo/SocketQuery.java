package cn.beichenhpy.websocketclient.pojo;

/**
 * @author beichenhpy
 * @since 2021-1-15
 * @description TODO 查询实体类，可自行修改，此处作为示例
 */
public class SocketQuery {

    private String miningAreaCode;

    private String summaryTypeCode;

    public SocketQuery(String miningAreaCode, String summaryTypeCode) {
        this.miningAreaCode = miningAreaCode;
        this.summaryTypeCode = summaryTypeCode;
    }

    public SocketQuery() {
    }

    public String getMiningAreaCode() {
        return miningAreaCode;
    }

    public void setMiningAreaCode(String miningAreaCode) {
        this.miningAreaCode = miningAreaCode;
    }

    public String getSummaryTypeCode() {
        return summaryTypeCode;
    }

    public void setSummaryTypeCode(String summaryTypeCode) {
        this.summaryTypeCode = summaryTypeCode;
    }

    @Override
    public String toString() {
        return "SocketQuery{" +
                "miningAreaCode='" + miningAreaCode + '\'' +
                ", summaryTypeCode='" + summaryTypeCode + '\'' +
                '}';
    }
}
