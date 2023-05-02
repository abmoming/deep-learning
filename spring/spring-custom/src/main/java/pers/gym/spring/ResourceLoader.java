package pers.gym.spring;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>资源加载器
 *
 * @author gym on 2022-12-16 15:12
 */
public class ResourceLoader {

    /**
     * 加载配置文件资源
     * bean的名称和全路径类名
     * 加载beans.properties配置文件的内容
     *
     * @return java.util.Map
     */
    public static Map<String, BeanDefinition> getResource() {
        HashMap<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
        Properties properties = new Properties();
        try {
            InputStream is = ResourceLoader.class.getResourceAsStream("/beans.properties");
            properties.load(is);
            for (String key : properties.stringPropertyNames()) {
                String className = properties.getProperty(key);
                Class<?> clazz = Class.forName(className);
                beanDefinitionMap.put(key, new BeanDefinition(key, clazz));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return beanDefinitionMap;
    }
}