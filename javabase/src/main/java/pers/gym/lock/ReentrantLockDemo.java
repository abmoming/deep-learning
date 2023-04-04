package pers.gym.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>ReentrantLockDemo
 *
 * @author gym on 2023-04-01 14:03
 */
public class ReentrantLockDemo {

    private final ReentrantLock mainLock = new ReentrantLock();

    public void demo() {
        // 第一种使用方式
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        // 第二种使用方式
        mainLock.lock();
    }
}