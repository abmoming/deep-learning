package pers.gym.jvm;

/**
 * <p>逃逸分析
 *
 * @author gym on 2023-03-10 14:38
 */
public class EscapeAnalysisTest {

    // 对象逃逸，会被外部引用
    public User test1() {
        User user = new User();
        user.setId(1);
        user.setName("moming");
        // TODO 保存到数据库
        return user;
    }

    // 对象不逃逸，跟随栈帧出栈而销毁
    public void test2() {
        User user = new User();
        user.setId(2);
        user.setName("justin");
        // TODO 保存到数据库
    }
}