package pers.gym.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pers.gym.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * <p>HystrixService
 *
 * @author gym on 2023-05-03 22:41
 */
@Service
public class HystrixService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 将会发起一个远程调用，去调用eureka provider中提供的/provider/list-provider接口
     * 有可能这个调用会失败的情况
     * 在这个方法上添加@HystrixCommand注解，配置fallbackMethod属性，这个属性表示方法调用失败时的临时替代方法
     * fallbackMethod属性填写的方法名要与创建的方法名称一致，创建的error方法的返回类型要与hystrix()方法返回类型一致
     * ，否则启动会报错
     */
    @HystrixCommand(fallbackMethod = "error", ignoreExceptions = ArithmeticException.class)
    public String hystrix() {
        int i = 1 / 0;
        return restTemplate.getForObject("http://EUREKA-PROVIDER/provider/list-provider", String.class);
    }

    @HystrixCommand(fallbackMethod = "error")
    public Future<String> async() {
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return restTemplate.getForObject("http://EUREKA-PROVIDER/provider/list-provider", String.class);
            }
        };
    }

    @HystrixCommand(fallbackMethod = "error2")
    @CacheResult // 表示该方法的请求结果会被缓存起来，默认情况下，缓存的key就是方法的参数，缓存的value就是方法的返回值
    public String cacheRequest(String param) {
        return restTemplate.getForObject("http://EUREKA-PROVIDER/provider/cache-request?param={1}", String.class, param);
    }

    @HystrixCommand(fallbackMethod = "error2")
    @CacheResult // 表示该方法的请求结果会被缓存起来，默认情况下，缓存的key就是方法的参数，缓存的value就是方法的返回值
    public String cacheRequest(@CacheKey String param, Integer age) {
        return restTemplate.getForObject("http://EUREKA-PROVIDER/provider/cache-request?param={1}", String.class, param);
    }

    @HystrixCommand
    @CacheRemove(commandKey = "cacheRequest")
    public String deleteUserByName(String param) {
        return null;
    }

    @HystrixCollapser(batchMethod = "userList", collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds", value = "200")})
    public Future<User> getUser(Long id) {
        return null;
    }

    @HystrixCommand
    public List<User> userList(List<Long> ids) {
        // 这里接收参数类型不用List.class的原因是List<Map<String, Object>>,这样的数据结构还要自己转
        User[] users = restTemplate.getForObject("http://EUREKA-PROVIDER/provider/list-user?ids={1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(Objects.requireNonNull(users));
    }

    public String error(Throwable t) {
        return "Remote call failure: " + t.getMessage();
    }

    public String error2(String param, Throwable t) {
        return "ERROE2, Remote call failure: " + param + ", msg: " + t.getMessage();
    }
}