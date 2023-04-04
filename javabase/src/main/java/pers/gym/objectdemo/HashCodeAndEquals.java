package pers.gym.objectdemo;

import java.util.HashMap;
import java.util.Objects;

/**
 * <p>重写hashCode和equals方法
 *
 * @author gym on 2023-03-29 11:26
 */
public class HashCodeAndEquals {

    private String name;
    private Integer age;

    public HashCodeAndEquals(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args) {
        HashCodeAndEquals m1 = new HashCodeAndEquals("moming", 18);
        HashCodeAndEquals m2 = new HashCodeAndEquals("moming", 18);
        System.out.println(m1 == m2);
        System.out.println(m1.equals(m2));
        System.out.println(m1.hashCode()==m2.hashCode());
        HashMap<HashCodeAndEquals, String> hashMap = new HashMap<>();
        hashMap.put(m1,"1");
        hashMap.put(m2,"2");
        System.out.println(hashMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        HashCodeAndEquals h = (HashCodeAndEquals) obj;
        return Objects.equals(name, h.name)
                && Objects.equals(age, h.age);
    }
}