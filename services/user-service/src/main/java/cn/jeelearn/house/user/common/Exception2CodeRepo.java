package cn.jeelearn.house.user.common;

import cn.jeelearn.house.user.exception.IllegalParamsException;
import cn.jeelearn.house.user.exception.WithTypeException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.reflect.FieldUtils;

/**
 * @Description: 将错误信息转为错误码信息
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
public class Exception2CodeRepo {

    private static final ImmutableMap<Object, RestCode> MAP = ImmutableMap.<Object, RestCode>builder()
            .put(IllegalParamsException.Type.WRONG_PAGE_NUM, RestCode.WRONG_PAGE)
            .put(IllegalStateException.class, RestCode.UNKNOWN_ERROR)
            .build();


    public static RestCode getCode(Throwable throwable){
        if (throwable == null){
            return RestCode.UNKNOWN_ERROR;
        }
        Object target = throwable;
        if (target instanceof WithTypeException){
            Object type = getType(throwable);
            if (type != null){
                target = type;
            }
        }
        RestCode restCode = MAP.get(target);
        if(restCode != null){
            return restCode;
        }
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if(rootCause != null){
            return getCode(rootCause);
        }
        return RestCode.UNKNOWN_ERROR;
    }


    private static Object getType(Throwable throwable){
        try {
            //forceAccess=true 强制异常
            return FieldUtils.readDeclaredField(throwable, "type", true);
        } catch (Exception e){
            return null;
        }
    }
}

