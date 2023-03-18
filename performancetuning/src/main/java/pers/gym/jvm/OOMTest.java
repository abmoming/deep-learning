package pers.gym.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>OOM测试
 *
 * @author gym on 2023-03-11 13:58
 */
@SuppressWarnings("all")
public class OOMTest {

    public static final List<User> list = new ArrayList<>();

    public static void main(String[] args) {
        int i = 0;
        int j = 0;
        while (true) {
            list.add(new User(i++, UUID.randomUUID().toString()));
            User user = new User(j--, UUID.randomUUID().toString());
        }
    }
}