package pers.gym.volatiledemo;

import sun.misc.Unsafe;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>使用volatile禁止重排序，多线程并发必须避免
 *
 * @author gym on 2023-03-22 11:13
 */
public class Singleton {

    // 使用volatile修饰uniqueInstance是很有必要的
    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
        // 先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (Objects.isNull(uniqueInstance)) {
            // 类对象加锁
            synchronized (Singleton.class) {
                if (Objects.isNull(uniqueInstance)) {
                    // 这段代码分为三个步骤：1.为uniqueInstance分配内存空间；2.初始化uniqueInstance；3.将uniqueInstance指向分配的内存地址；
                    // 由于JVM具有指令重排序的特性，执行顺序有可能变成 1->3->2。指令重排在单线程环境下会出现问题，但是在多线程环境下会导致一个线程还没有初始化的实例。
                    // 例如线程T1执行了1和3，此时T2调用getUniqueInstance()后发现uniqueInstance不为空，因此返回uniqueInstance，但此时uniqueInstance还未被初始化。
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(8, new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("test-" + threadNumber.getAndIncrement());
                return thread;
            }
        });

        for (int i = 0; i < 8; i++) {
            pool.execute(() -> {System.out.println(Thread.currentThread().getName() + " === " + Objects.isNull(Singleton.getUniqueInstance()));});
        }
        // 休息5秒，保证上面程序执行完成
        Thread.sleep(5000);
        pool.shutdown();
    }
}