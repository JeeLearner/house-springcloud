package cn.jeelearn.house.api.controller;

import cn.jeelearn.house.api.common.ResultMsg;
import cn.jeelearn.house.api.model.User;
import com.google.common.base.Objects;
import com.google.common.collect.Range;
import org.apache.commons.lang.StringUtils;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/12
 * @Version:v1.0
 */
public class UserHelper {

    /**
     * 账号信息校验
     *      这里是简单实现
     * @param account
     * @return
     */
    public static ResultMsg validate(User account){
        if (StringUtils.isBlank(account.getEmail())){
            return ResultMsg.errorMsg(("Email 为空"));
        }
        if (StringUtils.isBlank(account.getName())) {
            return ResultMsg.errorMsg("名字有误");
        }
        if (StringUtils.isBlank(account.getConfirmPasswd()) || StringUtils.isBlank(account.getPasswd())
                || !account.getPasswd().equals(account.getConfirmPasswd())) {
            return ResultMsg.errorMsg("密码 有误");
        }
        if (account.getPasswd().length() < 6) {
            return ResultMsg.errorMsg("密码大于6位");
        }
        if (account.getType() == null || !Range.closed(1,2).contains(account.getType())){
            return ResultMsg.errorMsg("类型有误");
        }
        return ResultMsg.successMsg("");
    }

    /**
     * 密码重置时信息校验
     * @auther: lyd
     * @date: 2018/12/14
     */
    public static ResultMsg validateResetPassword(String key, String password, String confirmPassword) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return ResultMsg.errorMsg("参数有误");
        }
        if (!Objects.equal(password, confirmPassword)) {
            return ResultMsg.errorMsg("密码必须与确认密码一致");
        }
        return ResultMsg.successMsg("");
    }

}

