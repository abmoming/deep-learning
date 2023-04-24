package pers.gym.volatiledemo;

import javax.lang.model.element.VariableElement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Volatile可以保证原子性吗？
 *
 * @author gym on 2023-03-22 12:12
 */
public class VolatileAtomicityDemo {

    public volatile static int inc = 0;

    private final Lock lock = new ReentrantLock();

    public void increase() {
        inc++;
    }

    // 使用synchronized改进
    /*public synchronized void increase() {
        inc++;
    }*/

    /*// 使用ReentrantLock改进
    public void increase() {
        lock.lock();
        try {
            inc++;
        } finally {
            lock.unlock();
        }
    }*/

    // volatile static boolean flag = true;
    volatile static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VolatileAtomicityDemo volatileAtomicityDemo = new VolatileAtomicityDemo();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        /*for (int i = 0; i < 5; i++) {
            pool.execute(() -> {
                for (int j = 0; j < 500; j++) {
                    volatileAtomicityDemo.increase();
                }
            });
        }
        // 休息1.5秒，保证上面程序执行完成
        Thread.sleep(1500);
        System.out.println(inc);
        pool.shutdown();*/

        int i = 0;
        while (i < 20) {
            if (i == 19) {
                // flag = false;
            }
            int finalI = i;
            pool.execute(() -> System.out.println("当前线程-" + finalI + "-"+flag));
            i++;
        }
        pool.shutdown();
    }
}