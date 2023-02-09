package pers.gym.jvm;

import java.lang.String;

/**
 * <p>math
 *
 * @author gym on 2022-11-19 16:19
 */
public class Math {

    public static final int initData = 666;
    public User user = new User();

    public int compute() { // 一个方法对应一块栈帧内存区域
        int a = 1;
        int b = 2;
        int c = (a + b) * 10;
        return c;
    }

    public static void main(String[] args) {
        Math math = new Math();
        math.compute();
    }
}
