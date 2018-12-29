package cn.jeelearn.house.user.exception;


/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/29
 * @Version:v1.0
 */
public class UserException extends RuntimeException implements WithTypeException {

    private static final long serialVersionUID = 6529013348928066744L;

    public UserException(Type type, String message){
        super(message);
        this.type = type;
    }

    public UserException(String message){
        super(message);
    }


    private Type type;

    private Type type(){
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUM,
        LACK_PARAMTER,
        USER_NOT_LOGIN,
        USER_NOT_FOUND,
        USER_AUTH_FAIL;
    }
}

