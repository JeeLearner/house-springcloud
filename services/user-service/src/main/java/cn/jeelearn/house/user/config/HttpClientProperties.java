package cn.jeelearn.house.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/26
 * @Version:v1.0
 */
@ConfigurationProperties(prefix="spring.httpclient")
public class HttpClientProperties {

    /**
     * 连接超时
     */
    private Integer connectTimeOut = 1000;
    /**
     * 读超时，即客户端和服务进行数据交互的时间
     */
    private Integer socketTimeOut = 1000000;

    private String agent = "agent";
    /**
     * 每个服务节点最大的连接数
     */
    private Integer maxConnPerRoute = 10;
    /**
     * 总最大连接数
     */
    private Integer maxConnTotaol   = 50;


    public Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Integer connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public Integer getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(Integer socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Integer getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(Integer maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public Integer getMaxConnTotaol() {
        return maxConnTotaol;
    }

    public void setMaxConnTotaol(Integer maxConnTotaol) {
        this.maxConnTotaol = maxConnTotaol;
    }
}

