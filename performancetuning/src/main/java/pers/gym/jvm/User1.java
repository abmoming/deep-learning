package pers.gym.jvm;

/**
 * <p>user
 *
 * @author gym on 2022-11-19 16:21
 */
public class User1 {

    private Integer id;
    private String name;

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
    protected void finalize() {
        //OOMTest
        System.out.println("关闭资源,user".concat(id.toString()).concat("即将被回收"));
    }

    public void print() {
        System.out.println("=======自己的加载器加载类调用方法======");
    }
}
