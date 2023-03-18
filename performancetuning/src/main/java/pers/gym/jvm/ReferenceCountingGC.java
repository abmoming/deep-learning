package pers.gym.jvm;

/**
 * <p>引用计数器GC
 *
 * @author gym on 2023-03-11 11:57
 */
public class ReferenceCountingGC {

    Object instance = null;

    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;
        objB.instance = objA;
        objA = null;
        objB = null;
    }
}