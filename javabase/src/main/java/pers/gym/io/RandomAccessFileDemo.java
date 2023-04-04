package pers.gym.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>随机访问流
 *
 * @author gym on 2023-04-01 18:11
 */
public class RandomAccessFileDemo {

    public static void main(String[] args){
        String path = RandomAccessFileDemo.class.getClassLoader().getResource("").getPath().substring(1);
        path = path + "input.txt";
        System.out.println(path);
        try(RandomAccessFile saveFile = new RandomAccessFile(new File(path), "rw")) {
            saveFile.seek(100);
            System.out.println((char) saveFile.read());
            saveFile.write("1".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}