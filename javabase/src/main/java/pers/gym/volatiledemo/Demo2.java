package pers.gym.volatiledemo;

import java.util.concurrent.TimeUnit;

/**
 * <p>Demo2
 *
 * @author gym on 2023-04-10 17:08
 */
public class Demo2 {

    public static void main(String[] args) {
        Moming m = new Moming();
        m.start();
        for (; ; ) {
            if (m.isFlag()) {
                System.out.println("有点东西");
            }
        }
    }
}

class Moming extends Thread {

    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        flag = true;
        System.out.println("flag=" + flag);
    }
}