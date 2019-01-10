package cn.jeelearn.house.api.controller;

import cn.jeelearn.house.api.common.ResultMsg;
import cn.jeelearn.house.api.inteceptor.UserContext;
import cn.jeelearn.house.api.model.Agency;
import cn.jeelearn.house.api.model.User;
import cn.jeelearn.house.api.service.AgencyService;
import cn.jeelearn.house.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/3
 * @Version:v1.0
 */
@Controller
@RequestMapping("/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AgencyService agencyService;


    //----------------------------注册流程-------------------------------------------
    @RequestMapping("/register")
    public String register(User account, ModelMap modelMap){
        if (account == null || account.getName() == null) {
            //modelMap.put("agencyList",  agencyService.getAllAgency());
            return "/user/accounts/register";
        }
        ResultMsg retMsg =  UserHelper.validate(account);
        if (retMsg.isSuccess()){
            boolean exist = userService.isExist(account.getEmail());
            if (!exist){
                userService.addAccount(account);
                modelMap.put("email", account.getEmail());
                return "/user/accounts/registerSubmit";
            } else {
                return "redirect:/accounts/register?" + ResultMsg.errorMsg("邮箱已被占用").asUrlParams();
            }
        } else {
            return "redirect:/accounts/register?" + ResultMsg.errorMsg("参数错误").asUrlParams();
        }
    }

    /**
     * 邮箱链接激活邮件
     * @auther: lyd
     * @date: 2018/12/13
     */
    @RequestMapping("/verify")
    public String verify(String key){
        boolean result = userService.enable(key);
        if (result){
            return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        } else {
            return "redirect:/accounts/register?" + ResultMsg.errorMsg("激活失败,请确认链接是否过期");
        }
    }

    //----------------------------登录流程-------------------------------------------
    @RequestMapping("/signin")
    public String login(HttpServletRequest req){
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username == null || password == null) {
            req.setAttribute("target", req.getParameter("target"));
            return "/user/accounts/signin";
        }
        User  user =  userService.login(username, password);
        if (user == null) {
            return "redirect:/accounts/signin?" + "username=" + username + "&" + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        }else {
            UserContext.setUser(user);
            return  StringUtils.isNotBlank(req.getParameter("target")) ? "redirect:" + req.getParameter("target") : "redirect:/index";
        }
    }

    @GetMapping("/logout")
    public String logout(){
        User user = UserContext.getUser();
        userService.logout(user.getToken());
        return "redirect:/index";
    }

    //----------------------------个人信息修改--------------------------------------
    @RequestMapping("/profile")
    public String profile(ModelMap  model){
        List<Agency> list =  agencyService.getAllAgency();
        model.addAttribute("agencyList", list);
        return "/user/accounts/profile";
    }

    @PostMapping("/profileSubmit")
    public String profileSubmit(HttpServletRequest req,User updateUser,ModelMap  model){
        if (updateUser.getEmail() == null) {
            return "redirect:/accounts/profile?" + ResultMsg.errorMsg("邮箱不能为空").asUrlParams();
        }
        User user = userService.updateUser(updateUser);
        UserContext.setUser(user);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }


}

