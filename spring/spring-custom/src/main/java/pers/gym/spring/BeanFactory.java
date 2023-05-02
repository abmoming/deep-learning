package pers.gym.spring;

import java.util.Map;
import java.util.Objects;

/**
 * <p>Bean对象工厂
 *
 * @author gym on 2022-12-19 14:09
 */
public class BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap;

    private final BeanRegister beanRegister;

    public BeanFactory() {
        this.beanDefinitionMap = ResourceLoader.getResource();
        this.beanRegister = new BeanRegister();
    }

    /**
     * 获取Bean对象
     *
     * @param beanName bean对象名称
     * @return java.lang.Object
     */
    public Object getBean(String beanName) {

        Object bean = beanRegister.getSingletonBean(beanName);
        if (Objects.nonNull(bean)) {
            return bean;
        }
        return createBean(beanDefinitionMap.get(beanName));
    }

    /**
     * 创建Bean对象
     *
     * @param beanDefinition bean定义
     * @return java.lang.Object
     */
    private Object createBean(BeanDefinition beanDefinition) {

        if (Objects.isNull(beanDefinition)) {
            return null;
        }

        Object bean;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
            beanRegister.registerSingletonBean(beanDefinition.getBeanName(), bean);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return bean;
    }
}
