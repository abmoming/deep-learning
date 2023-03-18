package pers.gym.jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * <p>计算对象大小
 *
 * @author gym on 2023-03-10 13:40
 */
public class JOLSample {

    public static void main(String[] args) {
        ClassLayout layout = ClassLayout.parseInstance(new Object());
        System.out.println(layout.toPrintable());

        System.out.println();
        ClassLayout layout1 = ClassLayout.parseInstance(new int[]{});
        System.out.println(layout1.toPrintable());

        System.out.println();
        ClassLayout layout2 = ClassLayout.parseInstance(new A());
        System.out.println(layout2.toPrintable());
    }

    // -XX:+UseCompressedOops          默认开启的压缩所有指针
    // -XX:+UseCompressedClassPointers 默认开启的压缩对象头里的类型指针Klass Pointer
    // Oops:Ordinary Object Pointers
    public static class A{
                        // 8B mark word
                        // 4B Klass Pointer
        int id;         // 4B
        String name;    // 4B 如果关闭压缩-XX:UseCompressedOops，则占用8B
        byte b;         // 1B
        Object o;       // 4B 如果关闭压缩-XX:UseCompressedOops，则占用8B
    }
}