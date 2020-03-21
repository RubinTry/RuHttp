package cn.rubintry.myhttp;

public class ResultModel {

    /**
     * data : {"admin":false,"chapterTops":[],"collectIds":[],"email":"","icon":"","id":54150,"nickname":"Rubintrys","password":"","publicName":"Rubintrys","token":"","type":0,"username":"Rubintrys"}
     * errorCode : 0
     * errorMsg :
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
