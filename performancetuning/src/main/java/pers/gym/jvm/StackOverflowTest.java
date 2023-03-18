package pers.gym.jvm;

/**
 * <p>栈溢出
 *
 * @author gym on 2023-03-09 11:38
 */
@SuppressWarnings("all")
public class StackOverflowTest {

    static int count = 0;

    static void redo() {
        count ++;
        redo();
    }

    public static void main(String[] args) {
        try {
            redo();
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(count);
        }
    }
}