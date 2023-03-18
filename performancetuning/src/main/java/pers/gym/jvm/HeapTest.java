package pers.gym.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>堆测试
 * 用于查看gc的情况
 *
 * @author gym on 2023-03-08 14:14
 */
@SuppressWarnings("all")
public class HeapTest {

    byte[] a = new byte[1024 * 100]; // 100KB

    public static void main(String[] args) throws InterruptedException {
        // heapTests可以理解为gc root
        List<HeapTest> heapTests = new ArrayList<>();
        while (true) {
            heapTests.add(new HeapTest());
            Thread.sleep(10);
        }
    }
}