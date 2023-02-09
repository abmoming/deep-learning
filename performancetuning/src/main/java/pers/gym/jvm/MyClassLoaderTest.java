package pers.gym.jvm;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * <p>MyClassLoaderTest
 *
 * @author gym on 2022-11-26 10:59
 */
public class MyClassLoaderTest {

    static class MyClassLoader extends ClassLoader {
        private final String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {

            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath.concat("/").concat(name).concat(".class"));
            // 若文件过大，容易溢出(最好还是使用缓冲区)
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException{

            try {
                byte[] data = loadByte(name);
                // defineClass将一个字节数组转为Class对象，这个字节数组是.class文件读取后最终的字节数组
                return defineClass(name,data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // 初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader classLoader = new MyClassLoader("E:/test");
        // E盘创建test/pers/gym/jvm几级目录，将User类的复制类User1.class丢入该目录
        Class<?> clazz = classLoader.loadClass("pers.gym.jvm.User1");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("print", null);
        method.invoke( obj, null);
        System.out.println(clazz.getClassLoader().getClass().getName());
    }
}
