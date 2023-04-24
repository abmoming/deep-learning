package pers.gym.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * @author gym on 2023-03-14 12:57
 */
public class SynchronizedTest {

    public static void main(String[] args) {

        /*new Thread(ClassLock::test1).start();
        new Thread(ClassLock::test2).start();*/

        ClassLock classLock = new ClassLock();
        new Thread(() -> {
            // new ClassLock().test1();
            // classLock.test1();
            classLock.test3();
        }).start();

        new Thread(() -> {
            // new ClassLock().test2();
            // classLock.test2();
            classLock.test4();
        }).start();
    }


}

class ClassLock {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");

    static Object obj = new Object();

    // public synchronized static void test1() {
    // public synchronized void test1() {
    public void test1() {
        synchronized (StaticLock.staticLock) {
            System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " begin...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " end...");
        }
    }

    // 注意：public static void test2(){ 不会互斥，因为此时test2没有使用类锁。
    // public synchronized static void test2() {
    // public synchronized void test2() {
    public void test2() {
        synchronized (StaticLock.staticLock) {
            System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " begin...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " end...");
        }
    }

    // 同一个实例对象调用test3和test4不会互斥
    // test3用的是对象锁 test4用的是类锁 是允许的，不会发生互斥现象
    public synchronized void test3() {
        System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " begin...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " end...");
    }

    public static synchronized void test4() {
        System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " begin...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sdf.format(new Date()) + " " + Thread.currentThread().getName() + " end...");
    }

}

class StaticLock {
    static final Object staticLock = new Object();
}
