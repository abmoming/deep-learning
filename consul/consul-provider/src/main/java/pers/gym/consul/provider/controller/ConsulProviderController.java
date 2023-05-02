package pers.gym.consul.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>ConsulProviderController
 *
 * @author gym on 2023-05-02 10:44
 */
@RestController
@RequestMapping("/consul")
public class ConsulProviderController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/provider")
    public String provider() {
        return "consul-provider, port: " + port;
    }
}