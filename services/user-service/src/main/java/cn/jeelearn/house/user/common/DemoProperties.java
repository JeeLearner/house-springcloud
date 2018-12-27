package cn.jeelearn.house.user.common;

/**
 * @Description: 建造者模式
 * @Auther: lyd
 * @Date: 2018/12/26
 * @Version:v1.0
 */
public class DemoProperties {

    private String username;
    private String password;

    DemoProperties(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * 入口 DemoProperties.custom()
     * @return
     */
    public static DemoProperties.Builder custom(){
        return new DemoProperties.Builder();
    }

    /**
     * 内部类，用于构建属性
     */
    public static class Builder{
        private String username;
        private String password;

        public DemoProperties.Builder setUsername(String username){
            this.username = username;
            return this;
        }
        public DemoProperties.Builder setPassword(String password){
            this.password = password;
            return this;
        }

        /**
         * 返回DemoProperties
         * @return
         */
        public DemoProperties build(){
            return new DemoProperties(username, password);
        }
    }

    /**
     * 测试
     * @auther: lyd
     * @date: 2018/12/26
     */
    public static void main(String[] args) {
        DemoProperties properties = new DemoProperties.Builder().setUsername("haha").setPassword("123").build();
        DemoProperties p = DemoProperties.custom().setUsername("hahaaaa").setPassword("12345").build();
        System.out.println(properties.getUsername());
    }
}

