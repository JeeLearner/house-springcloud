package cn.jeelearn.house.user.config.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
public class NewRuleConfig {

    @Autowired
    private IClientConfig ribbonClientConfig;

    /**
     * ping规则，访问服务端的/health路径确定是否存活，默认10秒ping一次
     * @param config
     * @return
     */
    @Bean
    public IPing ribbonPing(IClientConfig config){
        //isSecure不需要完全敏感的，设为false，ping一个url
        return new PingUrl(false, "/health");
    }

    /**
     * 定义负载均衡规则
     * @param config
     * @return
     */
    @Bean
    public IRule ribbonRule(IClientConfig config){
        //return new RandomRule();
        //AvailabilityFilteringRule的好处：
        // LoadBanlance拦截器在执行完之后，会对当前服务调用的结果做一些事情，下一些做负载均衡的时候，会首先选择上一次成功的服务器，而不会选择失败的服务器
        return new AvailabilityFilteringRule();
    }

}

