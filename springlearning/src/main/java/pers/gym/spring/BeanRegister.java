package pers.gym.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Bean对象注册器
 *
 * @author gym on 2022-12-18 20:08
 */
public class BeanRegister {

    // 单例Bean缓存
    private Map<String, Object> singletonMap = new HashMap<>(32);

    /**
     * 获取单例Bean对象
     *
     * @param beanName bean对象名称
     * @return java.lang.Object
     */
    public Object getSingletonBean(String beanName) {
        return singletonMap.get(beanName);
    }

    /**
     * 注册单例Bean
     * @param beanName bean对象名称
     * @param bean bean对象
     */
    public void registerSingletonBean(String beanName, Object bean) {
        if (singletonMap.containsKey(beanName)) {
            return;
        }
        singletonMap.put(beanName, bean);
    }
}
