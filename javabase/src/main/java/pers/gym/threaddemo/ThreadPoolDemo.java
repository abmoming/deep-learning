package pers.gym.threaddemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>自定义线程池
 *
 * @author gym on 2023-04-07 10:51
 */
public class ThreadPoolDemo implements ThreadFactory {

    private String name;
    private final AtomicInteger threadNum = new AtomicInteger(1);
    public static int count = 0;

    public ThreadPoolDemo() {
    }

    public ThreadPoolDemo(String name) {
        this.name = name;
    }

    public synchronized int add() {
        return ++count;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name + "-" + threadNum.getAndIncrement());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int total = 0;
        ThreadPoolDemo customThreadFactory = new ThreadPoolDemo("自定义线程池");
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5
                , 10
                , 1L
                , TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(100)
                , customThreadFactory
                , new ThreadPoolExecutor.CallerRunsPolicy());
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(threadPool.submit(() -> {
                int add = customThreadFactory.add();
                System.out.println(Thread.currentThread().getName()+"-自增后数量-"+add);
                return add;
            }));
        }
        for (Future<Integer> future : futures) {
            total += future.get();
        }
        threadPool.shutdown();
        System.out.println("5线程运行后的总数total: " + total);
        System.out.println("5线程运行后的总数count: " + count);
    }
}
