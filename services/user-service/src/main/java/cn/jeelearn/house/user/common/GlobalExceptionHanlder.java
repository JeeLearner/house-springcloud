package cn.jeelearn.house.user.common;

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
        //将错误信息转为错误码信息
        RestCode restCode = Exception2CodeRepo.getCode(throwable);
        RestResponse<Object> response = new RestResponse<>(restCode.code, restCode.msg);
        return response;
    }
}

