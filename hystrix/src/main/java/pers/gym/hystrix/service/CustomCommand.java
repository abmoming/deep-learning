package pers.gym.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * <p>CustomCommand 请求命令
 * 以继承的方式替代@HystrixCommand注解
 *
 * @author gym on 2023-05-04 12:33
 */
public class CustomCommand extends HystrixCommand<String> {

    private final RestTemplate restTemplate;

    String param;

    public CustomCommand(Setter setter, RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }

    public CustomCommand(Setter setter, RestTemplate restTemplate, String param) {
        super(setter);
        this.param = param;
        this.restTemplate = restTemplate;
    }

    /*@Override
    protected String run() throws Exception {
        int i = 1 / 0;
        return restTemplate.getForObject("http://EUREKA-PROVIDER/provider/list-provider", String.class);
    }*/

    @Override
    protected String run() throws Exception {
        return restTemplate.getForObject("http://EUREKA-PROVIDER/provider/cache-request?param={1}", String.class, param);
    }

    @Override
    protected String getCacheKey() {
        // 多个则配置多可即可，拼接方式自定义(通过构造函数传递过来)
        return param;
    }

    @Override
    protected String getFallback() {
        return "Remote call failure extends: " + getExecutionException().getMessage();
    }
}