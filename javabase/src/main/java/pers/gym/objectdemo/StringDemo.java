package pers.gym.objectdemo;

/**
 * <p>String
 *
 * @author gym on 2023-03-29 12:40
 */
public class StringDemo {

    public static void main(String[] args) {
        // final String str1 = "str"; // 常量化
        // final String str2 = "ing"; // 常量化，会发现str4==str3，str4==str5都为true
        String str1 = "str";
        String str2 = "ing";
        String str3 = "str" + "ing";
        String str4 = str1 + str2;
        String str5 = "string";
        System.out.println(str3 == str4);//false
        System.out.println(str3 == str5);//true
        System.out.println(str4 == str5);//false
    }
}