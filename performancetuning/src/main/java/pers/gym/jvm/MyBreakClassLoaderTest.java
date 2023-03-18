package pers.gym.jvm;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * <p>打破双亲委派机制
 *
 * @author gym on 2022-11-28 08:19
 */
public class MyBreakClassLoaderTest {

    public static class MyBreakClassLoader extends ClassLoader {

        private final String classPath;

        public MyBreakClassLoader(String classPath) {
            this.classPath = classPath;
        }

        public byte[] loadByte(String name) throws IOException {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath.concat("/").concat(name).concat(".class"));
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

            synchronized (getClassLoadingLock(name)) {
                // First, check if the class has already been loaded
                Class<?> c = findLoadedClass(name);
                if (c == null) {

                    long t0 = System.nanoTime();
                    // 不是自定义路径名称的，得让它的父加载器去加载相关jar包
                    if (!name.startsWith("pers.gym.jvm")) {
                        c = this.getParent().loadClass(name);
                    } else {
                        long t1 = System.nanoTime();
                        c = findClass(name);
                        // this is the defining class loader; record the stats
                        sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                        sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                        sun.misc.PerfCounter.getFindClasses().increment();
                    }
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static void main(String[] args) throws Exception {
            MyBreakClassLoader myBreakClassLoader = new MyBreakClassLoader("E:/test");
            Class<?> clazz = myBreakClassLoader.loadClass("pers.gym.jvm.User1");
            Object obj = clazz.newInstance();
            Method print = clazz.getDeclaredMethod("print", null);
            print.invoke(obj, null);
            // System.out.println(clazz.getClassLoader().getClass().getName());
            System.out.println(clazz.getClassLoader());

            // --------------------模拟tomcat的自定义类加载，每一个war都用的不同的类加载器
            MyBreakClassLoader myBreakClassLoader1 = new MyBreakClassLoader("E:/test1");
            Class<?> clazz1 = myBreakClassLoader1.loadClass("pers.gym.jvm.User1");
            Object obj1 = clazz1.newInstance();
            Method print1 = clazz1.getDeclaredMethod("print", null);
            print1.invoke(obj1, null);
            System.out.println(clazz1.getClassLoader());
        }
    }
}
