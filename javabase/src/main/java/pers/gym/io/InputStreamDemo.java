package pers.gym.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * <p>InputStreamDemo
 *
 * @author gym on 2023-03-30 17:04
 */
public class InputStreamDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final String PATH = Objects.requireNonNull(InputStreamDemo.class.getClassLoader().getResource("")).getPath();
        String FILE_PATH = PATH + "input.txt";
        FILE_PATH = FILE_PATH.substring(1);
        System.out.println(FILE_PATH);

        try (InputStream fis = Files.newInputStream(Paths.get(FILE_PATH))) {
            System.out.println("Number of remaining bytes: " + fis.available());
            int content;
            long skip = fis.skip(2);
            System.out.println("The actual number of bytes skipped: " + skip);
            System.out.print("The content read from file: ");
            while ((content = fis.read()) != -1) {
                System.out.print((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}