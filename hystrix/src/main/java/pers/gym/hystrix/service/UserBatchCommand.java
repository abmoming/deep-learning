package pers.gym.hystrix.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import pers.gym.entity.User;

import java.util.List;

/**
 * <p>用户批量请求
 *
 * @author gym on 2023-05-20 11:33
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {

    private final List<Long> ids;
    private final HystrixService hystrixService;

    protected UserBatchCommand(HystrixService hystrixService, List<Long> ids) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("userBatchCommand")));
        this.ids = ids;
        this.hystrixService = hystrixService;
    }

    @Override
    protected List<User> run() throws Exception {
        return hystrixService.userList(ids);
    }

    @Override
    protected String getFallbackMethodName() {
        return "error";
    }
}