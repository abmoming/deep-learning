package pers.gym.lock;

import java.util.concurrent.TimeUnit;

/**
 * <p>死锁
 *
 * @author gym on 2023-03-22 14:02
 */
public class DeadLockDemo {

    private static final Object RESOURCE1 = new Object(); // 资源1
    private static final Object RESOURCE2 = new Object(); // 资源2

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (RESOURCE1) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (RESOURCE2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "线程A").start();

        /*new Thread(() -> {
            synchronized (RESOURCE2) {
                System.out.println(Thread.currentThread() + "get resource2");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource1");
                synchronized (RESOURCE1) {
                    System.out.println(Thread.currentThread() + "get resource1");
                }
            }
        }, "线程B").start();*/

        // 线程B修改成以下，就不会造成死锁了
        new Thread(() -> {
            synchronized (RESOURCE1) {
                System.out.println(Thread.currentThread() + "get resource1");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resource2");
                synchronized (RESOURCE2) {
                    System.out.println(Thread.currentThread() + "get resource2");
                }
            }
        }, "线程B").start();
    }
}