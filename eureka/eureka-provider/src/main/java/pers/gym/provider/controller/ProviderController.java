package pers.gym.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}