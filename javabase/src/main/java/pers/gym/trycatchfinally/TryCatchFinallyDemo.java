package pers.gym.trycatchfinally;

import java.awt.print.PrinterGraphics;

/**
 * <p>
 *
 * @author gym on 2023-03-30 14:22
 */
public class TryCatchFinallyDemo {

    public static void main(String[] args) {

        /*try {
            System.out.println("Try to do something");
            throw new RuntimeException("RuntimeException");
        } catch (Exception e) {
            System.out.println("Catch Exception -> " + e.getMessage());
        } finally {
            System.out.println("Finally");
        }*/

        // System.out.println(func(2));

        breakFinallyReturn();
    }

    /**
     * finally块存在return的情况
     * try的return会暂存在本地变来那个中，当执行到finally语句汇总的return之后，
     * 这个本地变来那个的值就变为了finally语句中return返回值。
     *
     * @param value
     * @return
     */
    public static int func(int value) {

        try {
            return value * value;
        } finally {
            if (value == 2) {
                return 0;
            }
        }
    }

    /**
     * finally代码块一定会执行吗？不一定
     * 1.比如在finally之前虚拟机被终止运行的情况，finally中的代码就不会执行；
     * 2.程序所在的线程死亡；
     * 3.关闭CPU；
     * 以上三种情况都是不会执行finally代码块的
     */
    public static void breakFinallyReturn() {
        try {
            System.out.println("Try to do something");
            throw new RuntimeException("RuntimeException");
        } catch (Exception e) {
            System.out.println("Catch Exception -> " + e.getMessage());
            System.exit(1);
        } finally {
            System.out.println("Finally");
        }
    }
}