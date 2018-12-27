package cn.jeelearn.house.api.common;

/**
 * @Description: 返回码
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
public enum  RestCode {

    OK(0, "OK"),
    WRONG_PAGE(10100,"页码不合法");

    public final int code;
    public final String msg;

    private RestCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

