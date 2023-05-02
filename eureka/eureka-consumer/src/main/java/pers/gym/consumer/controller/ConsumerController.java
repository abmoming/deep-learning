package pers.gym.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>消费者控制层
 * 调用服务提供的API接口
 *
 * @author gym on 2023-04-25 15:06
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    @Qualifier("restTemplateOne")
    private RestTemplate restTemplateOne;

    @Autowired
    @Qualifier("restTemplateTwo")
    private RestTemplate restTemplateTwo;

    private static final AtomicInteger incr = new AtomicInteger();

    // 写死访问地址方式
    @GetMapping("/write-off")
    public String writeOff() {
        String providerUrl = "http://localhost:3100/provider/one-provider";
        return getHttpResponseContent(providerUrl);
    }

    @GetMapping("/discovery-client")
    public String discoveryClient() {

        // serviceId是服务名称，并不是实例名称
        List<ServiceInstance> instances = discoveryClient.getInstances("EUREKA-PROVIDER");
        if (instances == null || instances.size() == 0) {
            return "没有Provider服务可用";
        }
        ServiceInstance instance = instances.get(incr.getAndIncrement() % instances.size());
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/provider/list-provider";
        return getHttpResponseContent(url);
    }

    @GetMapping("/rest-template")
    public String restTemplate() {

        List<ServiceInstance> instances = discoveryClient.getInstances("EUREKA-PROVIDER");
        if (instances == null || instances.size() == 0) {
            return "没有Provider服务可用";
        }
        ServiceInstance instance = instances.get(incr.getAndIncrement() % instances.size());
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/provider/list-provider";
        return restTemplateOne.getForObject(url, String.class);
    }

    @GetMapping("/rest-template-load-balance")
    public String restTemplateLoadBalance() {

        // 应用程序名称
        final String serviceName = "EUREKA-PROVIDER";
        return restTemplateTwo.getForObject("http://" + serviceName + "/provider/list-provider", String.class);
    }

    public String getHttpResponseContent(String urlStr) {
        String content;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            content = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content;
    }
}