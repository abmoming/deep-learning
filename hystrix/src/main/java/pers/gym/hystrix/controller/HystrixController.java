package pers.gym.hystrix.controller;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pers.gym.entity.User;
import pers.gym.hystrix.service.CustomCommand;
import pers.gym.hystrix.service.HystrixService;
import pers.gym.hystrix.service.UserCollapseCommand;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <p>HystrixController
 *
 * @author gym on 2023-05-03 22:36
 */
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HystrixService hystrixService;

    @GetMapping("/fault-tolerant")
    public String faultTolerant() {
        return hystrixService.hystrix();
    }

    @GetMapping("/custom-command")
    public void customCommand() {

        try {
            CustomCommand customCommand = new CustomCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("gym")), restTemplate);
            // 直接执行
            String result1 = customCommand.execute();
            System.out.println("请求命令结果1: " + result1);
            CustomCommand customCommand2 = new CustomCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("gym")), restTemplate);
            // 先存进队列,后执行
            Future<String> future = customCommand2.queue();
            String result2 = future.get();
            System.out.println("请求命令结果2: " + result2);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/async")
    public String async() {
        Future<String> async = hystrixService.async();
        try {
            return async.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/cache-request")
    public String cacheRequest() {
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        String result = hystrixService.cacheRequest("gordon");
        result = hystrixService.cacheRequest("gordon");
        ctx.close();
        return result;
    }

    @GetMapping("/delete-cache-request")
    public String deleteCacheRequest() {
        final String param = "gordon";
        HystrixRequestContext hrc = HystrixRequestContext.initializeContext();
        // 第一次请求完已经缓存下来
        String result = hystrixService.cacheRequest(param);
        // 删除缓存
        result = hystrixService.deleteUserByName(param);
        // 第二次请求，虽然参数还是gordon，但是缓存数据已经没了，所以这一次eureka-provider还是会收到请求
        result = hystrixService.cacheRequest(param);
        hrc.close();
        return result;
    }

    @GetMapping("/custom-command-cache")
    public void customCommandCache() {

        HystrixRequestContext hrc = HystrixRequestContext.initializeContext();
        try {
            CustomCommand customCommand = new CustomCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("gym")), restTemplate, "gordon");
            // 直接执行
            String result1 = customCommand.execute();
            System.out.println("请求命令结果1: " + result1);
            CustomCommand customCommand2 = new CustomCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("gym")), restTemplate, "gordon");
            // 先存进队列,后执行
            Future<String> future = customCommand2.queue();
            String result2 = future.get();
            System.out.println("请求命令结果2: " + result2);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        hrc.close();
    }

    @GetMapping("/batch-collapse-command")
    public void batchCollapseCommand() throws ExecutionException, InterruptedException {
        HystrixRequestContext hrc = HystrixRequestContext.initializeContext();
        UserCollapseCommand cmd1 = new UserCollapseCommand(hystrixService, 1L);
        UserCollapseCommand cmd2 = new UserCollapseCommand(hystrixService, 2L);
        UserCollapseCommand cmd3 = new UserCollapseCommand(hystrixService, 3L);
        UserCollapseCommand cmd4 = new UserCollapseCommand(hystrixService, 4L);
        UserCollapseCommand cmd5 = new UserCollapseCommand(hystrixService, 5L);
        Future<User> q1 = cmd1.queue();
        Future<User> q2 = cmd2.queue();
        Future<User> q3 = cmd3.queue();
        Future<User> q4 = cmd4.queue();
        Future<User> q5 = cmd5.queue();
        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();
        User u4 = q4.get();
        User u5 = q5.get();

        /*TimeUnit.SECONDS.sleep(1L);
        UserCollapseCommand cmd5 = new UserCollapseCommand(hystrixService, 5L);
        Future<User> q5 = cmd5.queue();
        User u5 = q5.get();*/

        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        System.out.println(u4);
        System.out.println(u5);
    }

    @GetMapping("/batch-collapse-command-anno")
    public void batchCollapseCommandAnnotation() throws ExecutionException, InterruptedException {

        HystrixRequestContext hrc = HystrixRequestContext.initializeContext();
        Future<User> q1 = hystrixService.getUser(1L);
        Future<User> q2 = hystrixService.getUser(2L);
        Future<User> q3 = hystrixService.getUser(3L);
        User u1 = q1.get();
        User u2 = q2.get();
        User u3 = q3.get();
        System.out.println(u1);
        System.out.println(u2);
        System.out.println(u3);
        TimeUnit.SECONDS.sleep(1L);
        Future<User> q4 = hystrixService.getUser(4L);
        User u4 = q4.get();
        System.out.println(u4);
        hrc.close();
    }
}