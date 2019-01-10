package cn.jeelearn.house.house.common.rest;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/3
 * @Version:v1.0
 */
public class RestException extends RuntimeException {
    private static final long serialVersionUID = -2141901390411250987L;

    public RestException(String errorCode) {
        super(errorCode);
    }

    public RestException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public RestException(String errorCode, String errorMsg) {
        super(errorCode + ":" + errorMsg);
    }

    public RestException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode + ":" + errorMsg, cause);
    }

    public RestException(Throwable cause) {
        super(cause);
    }
}

