package cn.jeelearn.house.user.common;

/**
 * @Description: 返回码
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
public enum  RestCode {

    OK(0, "OK"),
    UNKNOWN_ERROR(1,"未知异常"),
    TOKEN_INVALID(2,"TOKEN失效"),
    USER_NOT_EXIST(3,"用户不存在"),
    WRONG_PAGE(10100,"页码不合法"),
    LACK_PARAMS(10101,"缺少参数");

    public final int code;
    public final String msg;

    private RestCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

