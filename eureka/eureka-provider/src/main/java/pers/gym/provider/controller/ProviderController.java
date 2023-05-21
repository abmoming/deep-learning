package pers.gym.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.gym.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>服务提供控制层
 *
 * @author gym on 2023-04-25 15:01
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Value("${server.port}")
    Integer port;

    @GetMapping("/one-provider")
    public String oneProvider() {
        return "只有一个Provider";
    }

    @GetMapping("/list-provider")
    public String listProvider() {
        return "集群Provider, 端口: " + port;
    }

    @GetMapping("/cache-request")
    public String cacheRequest(String param) {
        System.out.println("param: " + param + ", 现在时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return "param: " + param + ", 现在时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @GetMapping("/list-user")
    public List<User> listUser(String ids) {
        System.out.println(ids);
        User user;
        String[] split = ids.split(",");
        List<User> users = new ArrayList<>(split.length);
        for (String s : split) {
            user = new User();
            user.setId(Long.parseLong(s));
            user.setUsername("合并请求");
            users.add(user);
        }
        return users;
    }
}