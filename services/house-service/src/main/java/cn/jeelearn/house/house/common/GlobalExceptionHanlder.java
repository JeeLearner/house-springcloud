package cn.jeelearn.house.house.common;

import cn.jeelearn.house.house.exception.Exception2CodeMap;
import cn.jeelearn.house.house.exception.WithTypeException;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 全局异常处理器
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
@ControllerAdvice
public class GlobalExceptionHanlder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHanlder.class);

    /**
     * 错误信息处理
     * @param req
     * @param throwable
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public RestResponse<Object> handler(HttpServletRequest req, Throwable throwable){
        LOGGER.error(throwable.getMessage(), throwable);
        LOGGER.error(req.getRequestURL().toString() + " encounter exception or error");
        Object target = throwable;
        if (throwable instanceof WithTypeException) {
            Object type = getType(throwable);
            if (type != null) {
                target = type;
            }
        }

        //将错误信息转为错误码信息
        RestCode code =  Exception2CodeMap.getCode(target);
        RestResponse<Object> response = new RestResponse<>(code.code,code.msg);
        return response;
    }

    private Object getType(Throwable throwable){
        try {
            //forceAccess=true 强制异常
            return FieldUtils.readDeclaredField(throwable, "type", true);
        } catch (Exception e){
            return null;
        }
    }
}

