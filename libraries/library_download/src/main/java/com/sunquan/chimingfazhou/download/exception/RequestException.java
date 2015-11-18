package com.sunquan.chimingfazhou.download.exception;

public class RequestException extends Exception {
    static final long serialVersionUID = -3387516993124229135L;
    /**
     * 错误码，
     */
    private int errorCode;
    /**
     * 业务返回码
     */
    private int businessCode;

    public RequestException() {

    }

    public RequestException(int code, String message) {
        super(message);
        this.errorCode = code;
    }

    public RequestException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

}
