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
            new ClassLock().test1();
            // classLock.test1();
        }).start();

        new Thread(() -> {
            new ClassLock().test2();
            // classLock.test2();
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
}

class StaticLock{
    static final Object staticLock = new Object();
}
