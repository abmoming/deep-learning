package pers.gym.consul.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>ConsulConsumerController
 *
 * @author gym on 2023-05-02 21:09
 */
@RestController
@RequestMapping("/consul")
public class ConsulConsumerController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer")
    public String consumer() {

        // serviceId是service-name
        ServiceInstance choose = loadBalancerClient.choose("consul-provider");
        String result = restTemplate.getForObject(choose.getUri() + "/consul/provider", String.class);
        return "访问地址: " + choose.getUri() +
                ", 服务名称: " + choose.getServiceId() +
                ", 返回内容: " + result;
    }
}