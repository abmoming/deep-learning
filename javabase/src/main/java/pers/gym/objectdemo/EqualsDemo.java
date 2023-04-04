package pers.gym.objectdemo;

/**
 * <p>Object的equals方法
 *
 * @author gym on 2023-03-28 16:33
 */
public class EqualsDemo {

    public static void main(String[] args) {
        String a = new String("ab"); // a为一个引用
        String b = new String("ab"); // b为另一个引用，对象的内容一样
        String aa = "ab"; // 放在常量池中
        String bb = "ab"; // 从常量池中查找
        System.out.println(aa == bb); // true
        System.out.println(a == b); // false
        System.out.println(a.equals(b)); // true
        System.out.println(42 == 42.0); // true
        System.out.println("ab" == "ab"); // true
    }
}