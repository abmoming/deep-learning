package pers.gym.io.bigfiledownload.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>ThreadPoolUtil
 *
 * @author gym on 2023-04-01 15:44
 */
public class ThreadPoolUtil {

    // 同时执行的核心线程数
    private static final int CORE_POOL_SIZE = 5;
    // 同时执行最大核心线程数（新任务进来，执行线程 >= 同时执行核心线程数 && 任务数量 >= 队列容量；才会新生成同时执行的核心线程数的额外线程）
    private static final int MAX_POOL_SIZE = 10;
    // 队列容量
    private static final int QUEUE_CAPACITY = 100;
    // 超过同时执行核心线程数的额外线程，执行完任务可存活的时间，后被销毁
    private static final long KEEP_ALIVE_TIME = 1L;

    /**
     * 获取线程池
     *
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getThreadPool() {
        return getThreadPool(null, null, null, null, null);
    }

    /**
     * 获取线程池(重载)
     *
     * @param corePoolSize  核心线程数
     * @param maxPoolSize   最大核心线程数
     * @param keepAliveTime 核心线程数额外的空闲线程存活时间(秒)
     * @param queueCapacity 队列容量
     * @param threadName    自定义线程名称
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getThreadPool(Integer corePoolSize
            , Integer maxPoolSize, Long keepAliveTime
            , Integer queueCapacity, String threadName) {

        corePoolSize = corePoolSize == null ? CORE_POOL_SIZE : corePoolSize;
        maxPoolSize = maxPoolSize == null ? MAX_POOL_SIZE : maxPoolSize;
        queueCapacity = queueCapacity == null ? QUEUE_CAPACITY : queueCapacity;
        keepAliveTime = keepAliveTime == null ? KEEP_ALIVE_TIME : keepAliveTime;
        return new ThreadPoolExecutor(corePoolSize
                , maxPoolSize
                , keepAliveTime
                , TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(queueCapacity)
                , new CustomThreadFactory(threadName)
                , new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 自定以线程名称
     */
    private static class CustomThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNum = new AtomicInteger();
        private final String name;

        public CustomThreadFactory(String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, name + "-" + threadNum.getAndIncrement());
            // 当前线程是否守护线程，默认非守护线程(false)
            if (thread.isDaemon()) {
                thread.setDaemon(true);
            }
            // 设置默认优先级
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }
}