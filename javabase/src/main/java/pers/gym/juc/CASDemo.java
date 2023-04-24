package pers.gym.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * <p>CAS(Compare And Swap)案例
 * 1.解决CAS中的ABA问题
 *
 * @author gym on 2023-04-09 21:22
 */
public class CASDemo {

    // 可以将多个共享变量存进一个对象，再丢进AtomicReference实现多个共享变量的原子性
    private static final AtomicReference<Integer> atomicReference = new AtomicReference<>(100);

    private static final AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<Integer>(100, 1);

    public static void main(String[] args) throws InterruptedException {

        System.out.println("======ABA问题产生======");
        new Thread(() -> {
            // 把原值100改为101，再将100改为100，也就是ABA问题
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "线程A").start();

        new Thread(() -> {
            try {
                // 线程B睡眠3秒，让线程A完成ABA问题
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " " + atomicReference.compareAndSet(100, 2023) + "\t" + atomicReference.get());
        }, "线程B").start();

        TimeUnit.SECONDS.sleep(5);
        System.out.println("======解决ABA问题======");
        new Thread(() -> {
            /*try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "-第一个版本号: " + stampedReference.getStamp());
            stampedReference.compareAndSet(100, 101, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "-第二个版本号: " + stampedReference.getStamp());
            stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "-第三个版本号: " + stampedReference.getStamp());*/

            boolean flag;
            int[] arr = new int[1];
            for (int i = 0; i < 3; i++) {
                Integer reference = stampedReference.get(arr);
                int stamp = arr[0];
                if (i == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getName() + "-第" + (i + 1) + "个版本号: " + stamp);
                    continue;
                } else if (i == 1) {
                    flag = stampedReference.compareAndSet(reference, reference + 1, stamp, stamp + 1);
                } else {
                    flag = stampedReference.compareAndSet(reference, reference - 1, stamp, stamp + 1);
                }
                System.out.println(Thread.currentThread().getName() + "-第" + (i + 1) + "个值为: " + stampedReference.getReference() + ", 版本号: " + stampedReference.getStamp() + ", 执行状态: " + flag);
            }
        }, "线程C").start();

        new Thread(() -> {

            int[] arr = new int[1];
            Integer reference = stampedReference.get(arr);
            int stamp = arr[0];
            try {
                // 让线程C完成ABA操作
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            boolean flag = stampedReference.compareAndSet(reference, 2023, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "-值为: " + stampedReference.getReference() + ", 版本号: " + stampedReference.getStamp() + ", 执行状态: " + flag);
        }, "线程D").start();
    }
}