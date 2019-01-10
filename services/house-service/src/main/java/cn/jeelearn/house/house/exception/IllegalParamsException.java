package cn.jeelearn.house.house.exception;

/**
 * @Description: 非法参数异常
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
public class IllegalParamsException extends RuntimeException implements WithTypeException{
    private static final long serialVersionUID = 1425369421399337090L;

    private Type type;

    public IllegalParamsException(Type type, String msg){
        super(msg);
        this.type = type;
    }

    public Type type(){
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUM,//分页参数不合法
        WRONG_TYPE; //类型不合法
    }
}

