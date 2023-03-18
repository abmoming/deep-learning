package pers.gym.jvm;

/**
 * <p>测试触发GC
 * 添加运行JVM参数：-XX:+PrintGCDetails
 *
 * @author gym on 2023-03-10 15:44
 */
public class GCTest {

    public static void main(String[] args) {
        byte[] allocation1/*, allocation2, allocation3, allocation4, allocation5, allocation6*/;
        allocation1 = new byte[50000 * 1024]; // 50M
        /*allocation2 = new byte[10000 * 1024];  // 10M
        allocation3 = new byte[1000 * 1024];  // 1M
        allocation4 = new byte[1000 * 1024];  // 1M
        allocation5 = new byte[1000 * 1024];  // 1M
        allocation6 = new byte[1000 * 1024];  // 1M*/
    }
}