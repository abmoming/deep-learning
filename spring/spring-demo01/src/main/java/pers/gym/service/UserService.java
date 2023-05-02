package pers.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * @author gym on 2023-04-26 18:12
 */
@Component
public class UserService {

    @Autowired
    private OrderService orderService;

    public void print() {
        System.out.println(orderService);
    }
}