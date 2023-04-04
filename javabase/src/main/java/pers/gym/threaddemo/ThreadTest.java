package pers.gym.threaddemo;

import java.util.concurrent.*;

/**
 * <p>
 *
 * @author gym on 2023-03-20 10:38
 */
public class ThreadTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> ft = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "回调信息";
            }
        });
        // ft.run();

        new Thread(ft).start();
        String s = ft.get();
        System.out.println("结果: " + s);
    }
}