package pers.gym.jvm;

import java.util.concurrent.TimeUnit;

/**
 * <p>user
 *
 * @author gym on 2022-11-19 16:21
 */
public class User {

    private Integer id;
    private String name;

    public User() {
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected synchronized void finalize() throws InterruptedException {

        OOMTest.list.add(this);
        TimeUnit.SECONDS.sleep(1);
        if (!OOMTest.list.contains(this)) {
            System.out.println("关闭资源,user".concat(id.toString()).concat("即将被回收"));
        }
    }

    public void print() {
        System.out.println("=======自己的加载器加载类调用方法======");
    }
}