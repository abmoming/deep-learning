package pers.gym.jvm;

import java.lang.String;
import sun.misc.Launcher;

import java.net.URL;
import java.util.Arrays;

/**
 * <p>类加载器
 *
 * @author gym on 2022-11-20 16:37
 */
public class TestJDKClassLoader {

    public static void main(String[] args) {

        System.out.println(String.class.getClassLoader());
        System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader().getClass().getName());
        System.out.println(TestJDKClassLoader.class.getClassLoader().getClass().getName());

        System.out.println();
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = appClassLoader.getParent();
        ClassLoader bootStrapLoader = extClassLoader.getParent();
        System.out.println("the bootStrapLoader: " + bootStrapLoader);
        System.out.println("the extClassLoader: " + extClassLoader);
        System.out.println("the appClassLoader: " + appClassLoader);

        System.out.println();
        System.out.println("bootStrapLoader加载以下文件: ");
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        Arrays.stream(urLs).forEach(System.out::println);

        System.out.println();
        System.out.println("extClassLoader加载以下文件: ");
        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println();
        System.out.println("appClassLoader加载以下文件: ");
        System.out.println(System.getProperty("java.class.path"));
    }
}
