package cn.beichenhpy.websocket.pojo;


public class SocketQuery {

    private String miningAreaCode;

    private String summaryTypeCode;

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
