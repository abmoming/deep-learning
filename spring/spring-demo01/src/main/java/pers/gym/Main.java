package pers.gym;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.gym.service.UserService;
import pers.gym.config.AppConfig;

/**
 * <p>启动类
 *
 * @author gym on 2022-12-19 14:09
 */
public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService);
    }
}