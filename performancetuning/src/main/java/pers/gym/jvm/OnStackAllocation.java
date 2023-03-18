package pers.gym.jvm;

/**
 * <p>栈上分配，标量替换
 * 代码调用了1亿次allocation()，如果是分配到堆上，大概需要1GB以上堆空间，如果堆空间小于该值，必然会触发GC。
 *
 * 使用以下参数不会发生GC
 * -Xmx15M -Xms15M -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
 * 使用以下参数都会发生大量GC
 * -Xmx15M -Xms15M -XX:-DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
 * -Xmx15M -Xms15M -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:-EliminateAllocations
 *
 * @author gym on 2023-03-10 15:07
 */
public class OnStackAllocation {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            allocation();
        }
        long end = System.currentTimeMillis();
        System.out.println(((end - start) * 1.0 / 1000) + "s");
    }

    private static void allocation() {
        User user = new User();
        user.setId(1);
        user.setName("moming");
    }
}