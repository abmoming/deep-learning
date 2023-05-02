package pers.gym.spring;

import pers.gym.spring.entity.UserDao;

/**
 * <p>迷你Spring demo
 *
 * @author gym on 2022-12-19 16:51
 */
public class MiniSpringDemo {

    public static void main(String[] args) {
        // 1.创建bean工厂(同时完成加载资源、创建注册单例bean注册器的操作)
        BeanFactory beanFactory = new BeanFactory();

        // 2.第一次获取bean(通过反射创建bean，缓存bean)
        UserDao userDao = (UserDao) beanFactory.getBean("userDao");
        userDao.queryInfo();

        // 3.第二次获取bean(从缓存中获取bean)
        UserDao userDao2 = (UserDao) beanFactory.getBean("userDao");
        userDao2.queryInfo();
    }
}
