package cn.jeelearn.learn.fastjson;

import cn.jeelearn.learn.fastjson.base.BaseData;
import cn.jeelearn.learn.fastjson.base.Group;
import cn.jeelearn.learn.fastjson.base.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/26
 * @Version:v1.0
 */
public class Api {

    public static void test1(){
        Group group = BaseData.getGroup();
        List<User> users = BaseData.getUsers();
        String usersStr = JSON.toJSONString(users);

        //1.1序列化
        String groupStr = JSON.toJSONString(group);
        //1.2反序列化
        Group group1 = JSON.parseObject(groupStr, Group.class);
        //1.3泛型反序列化
        List<User> userList = JSON.parseObject(usersStr, new TypeReference<List<User>>() {});

        //{"id":0,"name":"admin","users":[{"id":2,"name":"guest"},{"id":3,"name":"root"}]}
        System.out.println(groupStr);
        //[{"id":2,"name":"guest"},{"id":3,"name":"root"}]
        System.out.println(usersStr);
    }


    public static void test2(){
        User user = BaseData.getUserWithDate();

        System.out.println("日期：" + user.getCreateDate());
        // 序列化
        String usersStr = JSON.toJSONString(user);
        System.out.println("序列化:" + usersStr);
        // 2.1序列化处理时间，方式一
        usersStr = JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss.SSS");
        //{"createDate":"2018-12-26 16:11:24.272","id":2,"name":"guest"}
        System.out.println("序列化处理时间，方式一:" + usersStr);
        // 2.2序列化处理时间，方式二：ISO-8601日期格式
        usersStr = JSON.toJSONString(user, SerializerFeature.UseISO8601DateFormat);
        System.out.println("序列化处理时间，方式二：ISO-8601日期格式:" + usersStr);
        // 2.3序列化处理时间，方式三：全局修改日期格式
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        usersStr = JSON.toJSONString(user, SerializerFeature.WriteDateUseDateFormat);
        System.out.println("序列化处理时间，方式三：全局修改日期格式:" + usersStr);

        /*
        日期：Wed Dec 26 16:16:47 CST 2018
        序列化:{"createDate":1545812207987,"id":2,"name":"guest"}
        序列化处理时间，方式一:{"createDate":"2018-12-26 16:16:47.987","id":2,"name":"guest"}
        序列化处理时间，方式二：ISO-8601日期格式:{"createDate":"2018-12-26T16:16:47.987+08:00","id":2,"name":"guest"}
        序列化处理时间，方式三：全局修改日期格式:{"createDate":"2018-12-26","id":2,"name":"guest"}
         */
    }

    public static void main(String[] args) {
        //test1();
        test2();
    }
}

