package cn.jeelearn.house.api.inteceptor;

import cn.jeelearn.house.api.common.CommonConstants;
import cn.jeelearn.house.api.dao.UserDao;
import cn.jeelearn.house.api.model.User;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{

    private static final String TOKEN_COOKIE = "token";

    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        Map<String, String[]> map = req.getParameterMap();
        map.forEach((k, v) -> {
            if (k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")) {
                req.setAttribute(k, Joiner.on(",").join(v));
            }
        });
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith("/static") || requestURI.startsWith("/error") ) {
            return true;
        }
        /*HttpSession session = httpServletRequest.getSession(true);
        User user =  (User)session.getAttribute(CommonConstants.USER_ATTRIBUTE);
        if (user != null) {
            UserContext.setUser(user);
        }*/
        Cookie cookie = WebUtils.getCookie(req, TOKEN_COOKIE);
        if (cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            User user = userDao.getUserByToken(cookie.getValue());
            if (user != null) {
                req.setAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE, user);
//          req.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
                UserContext.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView modelAndView) throws Exception {
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith("/static") || requestURI.startsWith("/error")) {
            return ;
        }
        User user = UserContext.getUser();
        if (user != null && StringUtils.isNotBlank(user.getToken())) {
            String token = requestURI.startsWith("/accounts/logout")? "" : user.getToken();
            Cookie cookie = new Cookie(TOKEN_COOKIE, token);
            cookie.setPath("/");
            //false 其他客户端也可以调用
            cookie.setHttpOnly(false);
            resp.addCookie(cookie);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserContext.remove();
    }

}

