package pers.gym.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>ThreadLocalDemo
 * 主要解决每个线程绑定自己的值
 *
 * @author gym on 2023-03-22 15:03
 */
public class ThreadLocalDemo implements Runnable {

    private static final ThreadLocal<SimpleDateFormat> FORMATTER = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " default formatter = " + FORMATTER.get().toPattern());
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FORMATTER.set(new SimpleDateFormat());
        System.out.println(Thread.currentThread().getName() + " modify formatter = " + FORMATTER.get().toPattern());
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadLocalDemo threadLocalDemo = new ThreadLocalDemo();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(threadLocalDemo, "thread-local-" + i);
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            thread.start();
        }
    }
}